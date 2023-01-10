# Video Transcriptor
is a java desktop application made using javafx, youtube api, voice rss and assemblyai.

# Dependencies
1. JDK-19
2. Javafx
3. Maven

# Features
1. convert text to audio 
2. convert video or audio to srt file
3. speech recognition convert recorded audio file to srt file
# How to run project

## 1. Install ffmpeg

For installation instructions check [FFMPEG](https://ffmpeg.org/download.html) website

## 2. Add ffmpeg to env path variable

## 3. Copy .env.example to .env
```bash
    cp .env.example .env
```
## 4. Create your api keys
1. [youtubeApi](https://developers.google.com/youtube/v3/getting-started#before-you-start)
2. [voiceRss](https://www.voicerss.org/)
3. [assemblyAi](https://www.assemblyai.com/docs/)

## 5. Add your api keys to .env
```
    VOICE_RSS_API_KEY=
    YOUTUBE_API_KEY=
    ASSEMBLY_AI_API_KEY=
```
## 6. Run main.java

