
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.app.constants.AiPrivacyConstants;
import com.example.app.constants.AppCategoryGroup;
import com.example.app.constants.ModelConfigDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ModelThresholdDetails {

    private static final String TAG = AiPrivacyConstants.TAG + ModelThresholdDetails.class.getSimpleName();

    private final Context context;
    HashMap<String, HashMap<String, ArrayList<Double>>> modelThresholdAndScalerDetails = new HashMap<String, HashMap<String, ArrayList<Double>>>();
    private JSONObject modelConfigDetails;
    private JSONObject modelThresholdDetails;
    private final HashMap<Integer, HashMap<Integer, ArrayList<Double>>> modelThresholdMap = new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>();
    private final HashMap<String, HashMap<String, ArrayList<Double>>> scalerDetailsMap = new HashMap<String, HashMap<String, ArrayList<Double>>>();

    public ModelThresholdDetails(Context context) {
        this.context = context;
        try {
            String jsonString = loadJSONFromAsset(ModelConfigDetails.MODEL_CONFIG_FILENAME);
            if (jsonString != null) {
                modelConfigDetails = new JSONObject(jsonString);
                String modelThresholdDetailsJsonString = loadJSONFromAsset(modelConfigDetails.getString(ModelConfigDetails.MODEL_CONFIG_JSON_MODEL_THRESHOLD_FILE_NAME_JSON));
                if (modelThresholdDetailsJsonString != null) {
                    modelThresholdDetails = new JSONObject(modelThresholdDetailsJsonString);
                    fillScalerDetails(modelThresholdDetails);
                    fillThresholdMap(modelThresholdDetails);
                } else {
                    modelConfigDetails = null;
                }
            } else {
                modelConfigDetails = null;
            }
        } catch (JSONException jsonException) {
            Log.e(TAG, "Error :: " + jsonException.getMessage());
            modelConfigDetails = null;
        }
    }

    private void fillScalerDetails(JSONObject modelThresholdDetailsJSON) {
        try {
            JSONObject scalerDetails = modelThresholdDetailsJSON.getJSONObject(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_DETAILS_KEY);
            JSONArray meanArray = scalerDetails.getJSONArray(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_MEAN_KEY);
            ArrayList<Double> mean = new ArrayList<Double>();
            for (int i = 0; i < meanArray.length(); i++) {
                mean.add(meanArray.getDouble(i));
            }
            JSONArray stdArray = scalerDetails.getJSONArray(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_STANDARD_DEVIATION_KEY);
            ArrayList<Double> std = new ArrayList<Double>();
            for (int i = 0; i < stdArray.length(); i++) {
                std.add(stdArray.getDouble(i));
            }
            this.scalerDetailsMap.put(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_DETAILS_KEY, new HashMap<String, ArrayList<Double>>());
            this.scalerDetailsMap.get(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_DETAILS_KEY).put(
                    ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_MEAN_KEY,
                    mean
            );
            this.scalerDetailsMap.get(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_DETAILS_KEY).put(
                    ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_STANDARD_DEVIATION_KEY,
                    std
            );
        } catch (Exception ex) {
            Log.e(TAG, "Error while fetching mean and standard deviation. Exception :: " + ex.getMessage());
        }
    }

    private void fillThresholdMap(JSONObject modelThresholdDETAILSJSON) {
        for (AppCategoryGroup.AppCategoryGroupName appCatGroupName : AppCategoryGroup.AppCategoryGroupName.values()) {
            int appCatGroupIndex = appCatGroupName.getValue();
            int bg = 0;
            int fg = 1;
            try {
                JSONObject appCategoryGroupThresholdMap = modelThresholdDETAILSJSON.getJSONObject(Integer.toString(appCatGroupIndex));
                JSONArray appCategoryGroupForegroundThreshold = appCategoryGroupThresholdMap.getJSONArray(Integer.toString(fg));
                JSONArray appCategoryGroupBackgroundThreshold = appCategoryGroupThresholdMap.getJSONArray(Integer.toString(bg));
                ArrayList<Double> thresholdArrayFG = new ArrayList<Double>();
                ArrayList<Double> thresholdArrayBG = new ArrayList<Double>();
                for (int i = 0; i < appCategoryGroupForegroundThreshold.length(); i++) {
                    thresholdArrayFG.add(appCategoryGroupForegroundThreshold.getDouble(i));
                }
                for (int i = 0; i < appCategoryGroupBackgroundThreshold.length(); i++) {
                    thresholdArrayBG.add(appCategoryGroupBackgroundThreshold.getDouble(i));
                }
                this.modelThresholdMap.put(
                        appCatGroupIndex,
                        new HashMap<Integer, ArrayList<Double>>()
                );
                this.modelThresholdMap.get(appCatGroupIndex).put(
                        fg,
                        thresholdArrayFG
                );
                this.modelThresholdMap.get(appCatGroupIndex).put(
                        bg,
                        thresholdArrayBG
                );
            } catch (Exception ex) {
                Log.e(TAG, "Error while fetching threshold json. Exception :: " + ex.getMessage());
            }
        }
    }

    public String getModelFileName() {
        if (this.modelConfigDetails == null) {
            Log.i(TAG, "Model Config Details is NULL");
            return null;
        }
        try {
            return this.modelConfigDetails.getString(ModelConfigDetails.MODEL_CONFIG_JSON_MODEL_NAME_KEY);
        } catch (Exception ex) {
            Log.e(TAG, "Error while fetching model file name :: " + ex.getMessage());
            return null;
        }
    }

    public String getModelVersion() {
        if (this.modelConfigDetails == null) {
            Log.i(TAG, "Model Config details is NULL");
            return null;
        }
        try {
            return this.modelConfigDetails.getString(ModelConfigDetails.MODEL_CONFIG_JSON_MODEL_MODEL_VERSION_KEY);
        } catch (Exception ex) {
            Log.e(TAG, "Error while fetching model file name :: " + ex.getMessage());
            return null;
        }
    }

    public ArrayList<Double> getThreshold(int appCategoryGroupIndex, int fg) {
        try {
            ArrayList<Double> threshold = this.modelThresholdMap.get(appCategoryGroupIndex).get(fg);
            return threshold;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<Double> getMean() {
        return this.scalerDetailsMap.get(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_DETAILS_KEY).get(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_MEAN_KEY);
    }

    public ArrayList<Double> getStd() {
        return this.scalerDetailsMap.get(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_DETAILS_KEY).get(ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_STANDARD_DEVIATION_KEY);
    }


    private String loadJSONFromAsset(String jsonFileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream file = assetManager.open(jsonFileName);
            byte[] formArray = new byte[file.available()];
            file.read(formArray);
            return new String(formArray);
        } catch (Exception ex) {
            Log.e(TAG, "Error while reading json file :: " + ex.getMessage());
            throw new RuntimeException("Reading JSON Failed for file :: " + jsonFileName);
        }
    }

}
