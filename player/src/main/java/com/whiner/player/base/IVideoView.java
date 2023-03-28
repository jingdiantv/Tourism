package com.whiner.player.base;

public interface IVideoView {

    void init();

    void addLoadingView();

    void showLoadingView();

    void hideLoadingView();

    IPlayerView getPlayerView();

    void setPlayerView(IPlayerView playerView);

    IPlayerControllerView getPlayerControllerView();

    void setPlayerControllerView(IPlayerControllerView playerControllerView);

    void create(boolean isLive);

    void readConfig();

    void setTitle(String title);

    void play(String url, boolean looping);

    String getUrl();

    void resume();

    void pause();

    void destroy();

}
