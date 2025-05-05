def filter_by_mood(logs_df, mood):
    if 'mood' not in logs_df.columns:
        raise ValueError("❌ Mood column missing in logs.")
    return logs_df[logs_df['mood'].str.lower() == mood.lower()]