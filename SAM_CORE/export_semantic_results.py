import os
import json
import faiss
import numpy as np
from datetime import datetime
from sentence_transformers import SentenceTransformer

# Paths
INDEX_PATH = "sam_index.faiss"
INDEX_LOGS_PATH = "sam_index_logs.json"
OUTPUT_FOLDER = "semantic_query_exports"
os.makedirs(OUTPUT_FOLDER, exist_ok=True)

# Load model + data
model = SentenceTransformer("sentence-transformers/paraphrase-MiniLM-L3-v2")
index = faiss.read_index(INDEX_PATH)
with open(INDEX_LOGS_PATH, "r", encoding="utf-8") as f:
    logs = json.load(f)

# Semantic query
query = "i feel worthless and don’t know what i need"
q_embedding = model.encode([query]).astype("float32")

# Search all
D, I = index.search(q_embedding, len(logs))

# Select top 1000 by distance
top_matches = I[0][:1000]
match_set = set(top_matches)

# Restore original log order while keeping only matched entries
ordered_results = []
for idx, log in enumerate(logs):
    if idx in match_set:
        ordered_results.append((idx, log, float(D[0][list(I[0]).index(idx)])))

# Save output
timestamp = datetime.now().strftime("%Y-%m-%d-%H-%M")
output_file = os.path.join(OUTPUT_FOLDER, f"{timestamp}-contextual-mixed.txt")

with open(output_file, "w", encoding="utf-8") as out:
    out.write(f"🔍 Semantic Query: {query}\n\n")
    for rank, (idx, log, dist) in enumerate(ordered_results):
        date = log.get("date", "no-date")
        role = log.get("role", "unknown").upper()
        mood = log.get("mood", "unknown").upper()
        emotion = log.get("emotion", "-")
        text = log.get("text", "").strip().replace("\n", " ")
        out.write(f"#{rank+1} | {date} | [{role}] [{mood}] {emotion} | score: {round(dist, 4)}\n{text}\n\n{'-'*80}\n\n")

print(f"✅ Exported {len(ordered_results)} conversation-aligned entries to:\n{output_file}")