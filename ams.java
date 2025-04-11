public class GsonTypeConverter {
    public static final Gson gson = new Gson();

    @TypeConverter
    public static String fromEventListToString(List<Event> eventList) {
        return gson.toJson(eventList);
    }

    @TypeConverter
    public static List<Event> fromStringToEventList(String json) {
        Type listType = new TypeToken<List<Event>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
}
