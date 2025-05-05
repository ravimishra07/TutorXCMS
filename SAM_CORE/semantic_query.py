import json
import faiss
import numpy as np
from sentence_transformers import SentenceTransformer

# Load index and logs
index = faiss.read_index("sam_index.faiss")
with open("sam_index_logs.json", "r", encoding="utf-8") as f:
    logs = json.load(f)

# Load sentence transformer
model = SentenceTransformer("all-MiniLM-L6-v2")

def search(query, top_k=5):
    q_embedding = model.encode([query]).astype("float32")
    D, I = index.search(q_embedding, top_k)

    print(f"\n🔍 Query: {query}\n")
    for rank, idx in enumerate(I[0]):
        log = logs[idx]
        print(f"#{rank+1} | {log['date']} | [{log['mood'].upper()}] {log['emotion']}")
        print(log['text'])
        print("-" * 80)

if __name__ == "__main__":
    while True:
        query = input("\n🧠 Enter search query (or 'exit'): ").strip()
        if query.lower() == "exit":
            break
        search(query)
