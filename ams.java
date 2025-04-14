
@Dao
public interface AiMinuteDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AiMinuteDataEntity aiMinuteSample);

    @Query("SELECT access_minute FROM " + TABLE_AI_MINUTE_DATA + " ORDER BY access_minute DESC LIMIT 1")
    long getLastPreProcessAccessMinute();

    @Query("SELECT * FROM ai_minute_data WHERE access_minute > :lastInfTime ")
    List<AiMinuteDataEntity> getMinuteDataFromLastInferenceTimestamp(long lastInfTime);

    @Query("DELETE FROM " + TABLE_AI_MINUTE_DATA)
    void deleteAll();
}
