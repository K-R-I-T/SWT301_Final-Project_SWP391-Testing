package com.cowards.onlyarts.resources.v4;

import com.cowards.onlyarts.repositories.token.TokenDTO;
import com.cowards.onlyarts.repositories.token.TokenERROR;
import com.cowards.onlyarts.repositories.user.UserDTO;
import com.cowards.onlyarts.services.FollowDAO;
import com.cowards.onlyarts.services.TokenDAO;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("v4/follow")
public class Follow {

    private static final TokenDAO tokenDao = TokenDAO.getInstance();
    private static final FollowDAO followDao = FollowDAO.getInstance();

    @POST
    @Path("{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(@HeaderParam("authtoken") String tokenString,
            @PathParam("userid") String userId) {
        try {
            TokenDTO token = tokenDao.getToken(tokenString);
            boolean check = followDao.addFollow(token.getUserId(), userId);
            if (check) {
                return Response.status(200).build();
            } else {
                throw new TokenERROR("Cannot follow this user");
            }
        } catch (TokenERROR ex) {
            return Response.status(401).entity(ex).build();
        }
    }

    @DELETE
    @Path("{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser(@HeaderParam("authtoken") String tokenString,
            @PathParam("userid") String userId) {
        try {
            TokenDTO token = tokenDao.getToken(tokenString);
            boolean check = followDao.unfollowUser(token.getUserId(), userId);
            if (check) {
                return Response.status(200).build();
            } else {
                throw new TokenERROR("Cannot unfollow this user");
            }
        } catch (TokenERROR ex) {
            return Response.status(401).entity(ex).build();
        }
    }

    @GET
    @Path("following/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowing(@PathParam("userid") String userId) {
        List<UserDTO> followingList = followDao.getFollowing(userId);
        return !followingList.isEmpty()
                ? Response.status(Response.Status.OK).entity(followingList).build()
                : Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("follower/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollower(@PathParam("userid") String userId) {
        List<UserDTO> followerList = followDao.getFollower(userId);
        return !followerList.isEmpty()
                ? Response.status(Response.Status.OK).entity(followerList).build()
                : Response.status(Response.Status.NO_CONTENT).build();

    }

}
