package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {

    Favorite findByRidAndUid(int rid, int uid);

    int getCount(int rid);

    void addFavorite(int rid, int uid);
}
