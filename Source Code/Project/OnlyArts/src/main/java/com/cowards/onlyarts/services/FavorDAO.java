package com.cowards.onlyarts.services;

import com.cowards.onlyarts.core.DBContext;
import com.cowards.onlyarts.repositories.artwork.ArtworkDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FavorDAO {

    private static final DBContext context = DBContext.getInstance();
    private static FavorDAO instance;
    //
    private static final String ADD_FAVORITE
            = "INSERT INTO [dbo].[Users_favor]"
            + " ([user_id], [artwork_id]) VALUES(?, ?)";
    //
    private static final String REMOVE_FAVORITE
            = "DELETE FROM [dbo].[Users_favor]"
            + "WHERE [user_id] = ? AND [artwork_id] = ?";
    private static final String GET_FAVORITE_ARTWORKS
            = "SELECT [dbo].[Artworks].[artwork_id],[owner_id],[cate_id],[name],"
            + " [description],[artwork_image], [price], [released_date], [status]"
            + " FROM [dbo].[Artworks] JOIN [dbo].[Users_favor] ON"
            + " [dbo].[Artworks].[artwork_id] = [dbo].[Users_favor].[artwork_id]"
            + " WHERE [dbo].[Users_favor].[user_id] = ?";

    private FavorDAO() {
    }

    private void logError(String message, Exception ex) {
        Logger.getLogger(FavorDAO.class.getName())
                .log(Level.SEVERE, message, ex);
    }

    public static FavorDAO getInstance() {
        if (instance == null) {
            instance = new FavorDAO();
        }
        return instance;
    }

    public boolean removeFavorite(String userId, String artworkId) {
        Connection conn = null;
        PreparedStatement stm = null;
        boolean res = false;
        try {
            conn = context.getConnection();
            stm = conn.prepareStatement(REMOVE_FAVORITE);
            stm.setString(1, userId);
            stm.setString(2, artworkId);
            res = stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            logError("Exception found on addReaction() method", ex);
            res = false;
        } finally {
            context.closeStatement(stm);
        }
        return res;
    }

    //
    public boolean addFavorite(String userId, String artworkId) {
        Connection conn = null;
        PreparedStatement stm = null;
        boolean res = false;
        try {
            conn = context.getConnection();
            stm = conn.prepareStatement(ADD_FAVORITE);
            stm.setString(1, userId);
            stm.setString(2, artworkId);
            res = stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            logError("Exception found on addFavorite() method", ex);
            res = false;
        } finally {
            context.closeStatement(stm);
        }
        return res;
    }
    //

    public List<ArtworkDTO> getFavoriteArtworks(String userId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<ArtworkDTO> artworks = new ArrayList<>();
        try {
            conn = context.getConnection();
            stm = conn.prepareStatement(GET_FAVORITE_ARTWORKS);
            stm.setString(1, userId);
            rs = stm.executeQuery();
            ArtworkDTO artwork = null;
            while (rs.next()) {
                artwork = new ArtworkDTO();
                artwork.setArtworkId(rs.getString(1));
                artwork.setOwnerId(rs.getString(2));
                artwork.setCateId(rs.getString(3));
                artwork.setName(rs.getString(4));
                artwork.setDescription(rs.getString(5));
                artwork.setArtworkImage(rs.getString(6));
                artwork.setPrice(rs.getFloat(7));
                artwork.setReleasedDate(rs.getDate(8));
                artwork.setStatus(rs.getInt(9));
                artworks.add(artwork);
            }
        } catch (SQLException ex) {
            logError("Exception found on getFavoriteArtworks() method", ex);
        } finally {
            context.closeResultSet(rs);
            context.closeStatement(stm);
        }
        return artworks;
    }
}
