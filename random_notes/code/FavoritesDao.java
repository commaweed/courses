@Repository("x-web-ref.FavoritesDao")
public interface FavoritesDao extends
   MongoRepository<Favorite, String>, FavoritesDaoCustom, PagingAndSortingRepository<Favorite, String>
{
   @Query(
       value="{" + SubjectFavorite.FIELD_NAME_SUBJECT + ": ?0" + ", " + blah + ": ?1" + ", " + blah3 + ": ?2 }", delete=true)
      void deleteSubjectFavoriteForUser(String subject, String parentValue, String user);
}
