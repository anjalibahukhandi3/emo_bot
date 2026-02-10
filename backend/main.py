from fastapi import FastAPI
from pydantic import BaseModel
import random

app = FastAPI()

class EmotionRequest(BaseModel):
    text: str

class EmotionResponse(BaseModel):
    emotion: str
    confidence: float

@app.post("/analyze", response_model=EmotionResponse)
async def analyze_emotion(request: EmotionRequest):
    text = request.text.lower()
    
    # Simple keyword-based logic for demonstration
    # In a real app, you would use a model like BERT or a library like TextBlob/NLTK here
    
    if any(word in text for word in ["happy", "good", "great", "love", "awesome", "excellent"]):
        return EmotionResponse(emotion="Happy", confidence=random.uniform(0.7, 0.99))
    
    elif any(word in text for word in ["sad", "bad", "terrible", "cry", "awful", "depressed"]):
        return EmotionResponse(emotion="Sad", confidence=random.uniform(0.6, 0.95))
        
    elif any(word in text for word in ["angry", "mad", "hate", "furious", "annoyed"]):
        return EmotionResponse(emotion="Angry", confidence=random.uniform(0.6, 0.9))
        
    elif any(word in text for word in ["fear", "scared", "afraid", "terrified", "nervous"]):
        return EmotionResponse(emotion="Fear", confidence=random.uniform(0.5, 0.85))
        
    else:
        return EmotionResponse(emotion="Neutral", confidence=random.uniform(0.4, 0.8))

if __name__ == "__main__":
    import uvicorn
    # Host 0.0.0.0 allows access from external devices (like Android Emulator)
    uvicorn.run(app, host="0.0.0.0", port=8000)
