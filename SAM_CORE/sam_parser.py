import os, json, uuid
from datetime import datetime
from tqdm import tqdm
from transformers import pipeline

# Load sentiment classifier
sentiment_pipe = pipeline("sentiment-analysis", model="cardiffnlp/twitter-roberta-base-sentiment")

def transformer_mood_tag(text):
    try:
        result = sentiment_pipe(text[:512])[0]  # truncate long messages
        label = result['label'].lower()
        score = result['score']

        if label == "positive":
            return "high", "relief"
        elif label == "negative":
            return "low", "anxiety"
        else:
            return "neutral", "neutral"
    except Exception:
        return "neutral", "neutral"

def main():
    with open("conversations.json", "r", encoding="utf-8") as f:
        data = json.load(f)

    output_dir = "sam_logs_output"
    os.makedirs(output_dir, exist_ok=True)

    logs_by_month = {}

    for conv in tqdm(data, desc="Parsing Conversations"):
        create_time = conv.get("create_time", 0)
        if "mapping" not in conv:
            continue

        for node in conv["mapping"].values():
            msg = node.get("message")
            if not msg:
                continue

            role = msg.get("author", {}).get("role", "")
            parts = msg.get("content", {}).get("parts", [])
            if not parts:
                continue

            text = "\n".join([str(p) for p in parts]).strip()
            if not text:
                continue

            ts = msg.get("create_time", create_time)
            dt = datetime.fromtimestamp(ts)
            month_key = dt.strftime("%Y-%m")

            mood, emotion = transformer_mood_tag(text)

            logs_by_month.setdefault(month_key, []).append({
                "uuid": str(uuid.uuid4()),
                "timestamp": int(ts * 1000),
                "date": dt.strftime("%Y-%m-%d"),
                "text": f"[{role.upper()}] {text}",
                "tags": [],
                "mood": mood,
                "emotion": emotion,
                "energyTier": mood,
                "stateTag": "reflective",
                "isDraft": False,
                "source": "chatgpt_export",
                "rating": None
            })

    for month, logs in logs_by_month.items():
        with open(os.path.join(output_dir, f"{month}.jsonl"), "w", encoding="utf-8") as f:
            for log in logs:
                f.write(json.dumps(log) + "\n")

    print(f"\n✅ Done. Logs saved in: {output_dir}")

if __name__ == "__main__":
    main()