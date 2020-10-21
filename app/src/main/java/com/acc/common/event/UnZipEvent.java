package com.acc.common.event;

/**
 * Created by androider on 2018/8/31.
 * 内容：
 */
public class UnZipEvent {

    private int progress;

    public UnZipEvent(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
