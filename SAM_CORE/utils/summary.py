def generate_summary(logs_df, target_month):
    logs_df['month'] = logs_df['date'].dt.strftime('%Y-%m')
    month_logs = logs_df[logs_df['month'] == target_month]

    if month_logs.empty:
        return "⚠️ No entries found for that month."

    summary = []
    summary.append(f"📝 Total entries: {len(month_logs)}")
    mood_counts = month_logs['mood'].value_counts()
    summary.append("📊 Mood distribution:")
    for mood, count in mood_counts.items():
        summary.append(f"  • {mood}: {count}")
    
    sample_texts = month_logs['text'].head(5).tolist()
    summary.append("\n🧠 Sample entries:")
    for i, text in enumerate(sample_texts):
        summary.append(f"{i+1}. {text[:100]}{'...' if len(text) > 100 else ''}")
    
    return "\n".join(summary)