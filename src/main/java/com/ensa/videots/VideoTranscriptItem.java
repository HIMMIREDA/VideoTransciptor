package com.ensa.videots;

public class VideoTranscriptItem {
    public String id;
    public String audio_url;
    public String status;
    public String text;
    public String language_code;

    public String upload_url;

    public String getAudio_url() {
        return audio_url;
    }

    public String getId() {
        return id;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code == null ? "en" : language_code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUpload_url() {
        return upload_url;
    }

    public void setUpload_url(String upload_url) {
        this.upload_url = upload_url;
    }
}
