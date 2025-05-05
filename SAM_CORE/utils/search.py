from fuzzywuzzy import process

def fuzzy_search(logs_df, term, limit=10):
    if 'text' not in logs_df.columns:
        return "❌ No 'text' field in logs."
    texts = logs_df['text'].tolist()
    results = process.extract(term, texts, limit=limit)
    return "\n".join([f"{score}%: {match}" for match, score in results])