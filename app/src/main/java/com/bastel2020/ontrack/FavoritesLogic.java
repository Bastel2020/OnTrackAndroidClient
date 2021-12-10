package com.bastel2020.ontrack;

import android.content.Context;
import android.widget.ImageButton;

import java.util.List;

public class FavoritesLogic {

    public static void ChangeFavoritesButtonState(Context context, ImageButton button, int placeId, String placeName, String placeLocation)
    {
        if (DbContext.ContainsPlaceInFavorites(placeId))
        {
            DbContext.RemovePlaceFromFavorites(placeId);
            button.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
        }
        else
        {
            DbContext.AddPlaceToFavorites(placeId, placeName, placeLocation);
            button.setBackgroundResource(R.drawable.favorite_checked);
        }

        if (ServerRequester.IsValidToken(context))
        {
            ServerRequester.ChangeFavoriteState(context, placeId);
        }
    }

    public static boolean ContainsPlace(int placeId)
    {
        return DbContext.ContainsPlaceInFavorites(placeId);
    }

    public static List<DbContext.TripLiteModel> GetFavorites()
    {
        return DbContext.GetFavoritesPlaces();
    }

    public static void SyncFavorites(List<ServerRequester.PlaceInfo> places)
    {
        for (ServerRequester.PlaceInfo itVar: places) {
            if (!ContainsPlace(itVar.Id))
            {
                DbContext.AddPlaceToFavorites(itVar.Id, itVar.Name, itVar.TextLocation);
            }
        }
    }
}
