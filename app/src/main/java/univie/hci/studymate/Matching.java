package univie.hci.studymate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Init a Matching obj. with the current users on the app and call math_one(User) or match_more(User)
 * */
public class Matching {

    private Collection<User> users;


    Matching(Collection<User> users) {
        this.users = users;
    }

    /**
     * @param user The user with the match algo will run on
     * @return returns a sorted list of users, sorted by the most common Tag
     * */
    public Collection<User> match_more(User user) {
        return users.stream()
                .filter(other -> !other.equals(user)) // To not return the input user
                .sorted((userOne, userTwo) -> {

                    int uniComparison = compareUniversities(user.getUniversity(), userOne.getUniversity(), userTwo.getUniversity());
                    if (uniComparison != 0) {
                        // would we not only sort after this rule if uniComparison is not 0?
                        return uniComparison;
                    }
                    return Integer.compare(
                            countCommonTags(user, userTwo),
                            countCommonTags(user, userOne));
                }) // Sort in descending order
                .collect(Collectors.toList());
    }

    private int countCommonTags(User user1, User user2) {
        Set<Tag> user1Tags = new HashSet<>(user1.getTags());
        Set<Tag> user2Tags = new HashSet<>(user2.getTags());

        user1Tags.retainAll(user2Tags);
        return user1Tags.size();
    }

    private int compareUniversities(University mainUserUni, University uniUserOne, University uniUserTwo) {
        boolean userOneSameUni = mainUserUni == uniUserOne;
        boolean userTwoSameUni = mainUserUni == uniUserTwo;

        if (userOneSameUni && !userTwoSameUni) {
            return 1;
        } else if (!userOneSameUni && userTwoSameUni) {
            return -1;
        }
        return 0; // both users have the same uni
    }
}
