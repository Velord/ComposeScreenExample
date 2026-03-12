package com.velord.gateway.movie

import com.velord.model.movie.Movie
import java.util.Calendar

private fun createCalendar(year: Int, month: Int, day: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Months are 0-based in Calendar
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return calendar
}

internal val testMovieRoster = listOf<Movie>(
    Movie(
        id = -5,
        title = "Star Wars",
        description = "A long time ago in a galaxy far, far away...",
        isLiked = true,
        createCalendar(1977, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
)

internal val testMovieRoster2 = listOf(
    Movie(
        id = 1,
        title = "Star Wars",
        description = "A long time ago in a galaxy far, far away...",
        isLiked = true,
        createCalendar(1977, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 2,
        title = "The Lord of the Rings",
        description = "One ring",
        isLiked = false,
        createCalendar(2001, 6, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 3,
        title = "The Shawshank Redemption",
        description="Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
        isLiked = false,
        createCalendar(1994, 9, 23),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 4,
        title = "The Godfather",
        description = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
        isLiked = false,
        createCalendar(1972, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 5,
        title = "The Dark Knight",
        description = "When the menace known as the Joker wreaks havoc and chaos on the",
        isLiked = true,
        createCalendar(2008, 7, 18),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 6,
        title = "The Matrix",
        description = "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
        isLiked = false,
        createCalendar(1999, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 7,
        title = "Inception",
        description = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
        isLiked = true,
        createCalendar(2010, 7, 16),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 8,
        title = "Interstellar",
        description = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
        isLiked = false,
        createCalendar(2014, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 9,
        title = "The Prestige",
        description = "After a tragic accident, two stage magicians engage in a battle to create the ultimate illusion while sacrificing everything they have to outwit each other.",
        isLiked = true,
        createCalendar(2006, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 10,
        title = "The Departed",
        description = "An undercover cop and a mole in the police attempt to identify each other while infiltrating an Irish gang in South Boston.",
        isLiked = false,
        createCalendar(2006, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 11,
        title = "The Lion King",
        description = "Lion cub and future king Simba searches for his identity. His eagerness to please others and penchant for testing his boundaries sometimes gets him into trouble.",
        isLiked = false,
        createCalendar(1994, 1, 1),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 13,
        title = "The Godfather: Part II",
        description="The story of the Corleone family in America, focusing on the transformation of Michael Corleone from reluctant family outsider to ruthless mafia patriarch.",
        isLiked = false,
        createCalendar(1974, 12, 15),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 14,
        title = "Good Will Hunting",
        description="Will Hunting, a young janitor at MIT, has a gift for mathematics but needs help from a therapist to find direction in his life.",
        isLiked = false,
        createCalendar(1997, 12, 10),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 15,
        title = "Schindler's List",
        description="The true story of Oskar Schindler, a German businessman who saved the lives of over 1100 Jews during the Holocaust by employing them in his factories.",
        isLiked = false,
        createCalendar(1993, 12, 15),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 16,
        title = "12 Angry Men",
        description="A jury must come to a unanimous decision in the case of a young man accused of murdering his father.",
        isLiked = false,
        createCalendar(1957, 4, 11),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 17,
        title = "Pulp Fiction",
        description="The lives of two mob hit men, a boxer, and a wife of a gangster become intertwined in a series of violent events.",
        isLiked = false,
        createCalendar(1994, 9, 23),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 18,
        title = "The Lord of the Rings: The Return of the King",
        description="Gandalf and Aragorn lead the World of Men into a final battle against Sauron's evil forces. Frodo and Sam must travel deep into Mordor to destroy the One Ring and end the threat of Sauron forever.",
        isLiked = false,
        createCalendar(2003, 12, 17),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 19,
        title = "Parasite",
        description="The members of a poor family scheme to become employed by a wealthy family.",
        isLiked = false,
        createCalendar(2019, 5, 30),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 20,
        title = "The Princess Bride",
        description="Westley, a farmhand with nothing to his name except his love for Princess Buttercup, must face the evil Prince Humperdinck, his three deadly henchmen, and a cunning Sicilian in order to win her hand in marriage.",
        isLiked = false,
        createCalendar(1987, 9, 25),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 21,
        title = "Eternal Sunshine of the Spotless Mind",
        description="When Joel Barish discovers that his girlfriend Clementine has erased all memories of their relationship, he undergoes the same procedure to erase her from his mind. However, as the procedure begins, Joel realizes that he may not want to forget everything.",
        isLiked = false,
        createCalendar(2004, 3, 10),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 22,
        title = "Spirited Away",
        description="A young girl, Chihiro Ogino, finds herself in the world of spirits. After her parents are transformed into pigs by the witch Yubaba, Chihiro takes a job working in Yubaba's bathhouse to find a way to free herself and her parents and return to the human world.",
        isLiked = false,
        createCalendar(2001, 7, 20),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 23,
        title = "The Farewell",
        description="Billi, a young Chinese-American writer, travels to China to visit her ailing grandmother. What starts as an emotional reunion turns into an unexpected journey as Billi grapples with cultural differences and family secrets.",
        isLiked = false,
        createCalendar(2019, 4, 26),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 24,
        title = "Mad Max: Fury Road",
        description="In a post-apocalyptic wasteland, a woman named Imperator Furiosa rebels against a tyrannical ruler in order to help five enslaved women escape from his clutches.",
        isLiked = false,
        createCalendar(2015, 5, 14),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 25,
        title = "Amélie",
        description="A shy waitress in a Parisian café decides to anonymously change the lives of the people around her for the better, but struggles to find happiness for herself.",
        isLiked = false,
        createCalendar(2001, 4, 25),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 26,
        title = "Crouching Tiger, Hidden Dragon: Sword of Destiny",
        description="The legendary warrior, Li Mu Bai, entrusts his powerful Green Destiny sword to his security chief, Shu Lien. However, the sword is stolen, and Li Mu Bai must find it before it falls into the wrong hands.",
        isLiked = false,
        createCalendar(2016, 2, 8),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 27,
        title = "The Grand Budapest Hotel",
        description="The story of a legendary concierge at a renowned European hotel between the wars and his relationship with a young lobby boy.",
        isLiked = false,
        createCalendar(2014, 2, 6),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 28,
        title = "WALL-E",
        description="In a dystopian future where Earth is overrun with garbage, a lonely robot named WALL-E encounters a sleek robot named EVE and follows her into outer space on a quest to save humanity.",
        isLiked = false,
        createCalendar(2008, 6, 27),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 29, // Continuing from previous ID
        title = "Avengers: Endgame",
        description="After the devastating events of Avengers: Infinity War (2018), the remaining Avengers assemble once more in order to undo Thanos' actions and restore balance to the universe.",
        isLiked = false,
        createCalendar(2019, 4, 26),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 30,
        title = "Captain Marvel",
        description="Carol Danvers becomes one of the universe's most powerful heroes when she absorbs the energy from an alien engine. Set in the 1990s, she finds herself in a war between two alien races and must reunite with an old friend to learn more about her past while saving the planet from a dangerous threat.",
        isLiked = false,
        createCalendar(2019, 3, 8),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 31,
        title = "Aladdin",
        description="A kind-hearted street urchin named Aladdin embarks on an adventure after finding a magical lamp that houses a powerful genie.",
        isLiked = false,
        createCalendar(2019, 5, 24),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 32,
        title = "The Lion King",
        description="A live-action remake of the 1994 Disney classic, telling the story of Simba's journey to become king.",
        isLiked = false,
        createCalendar(2019, 7, 19),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 33,
        title = "Toy Story 4",
        description="Woody, Buzz Lightyear, and the gang find themselves on a new adventure when Bonnie takes a road trip with her family. Along the way, they encounter a new toy named Forky who doesn't understand why he's a toy.",
        isLiked = false,
        createCalendar(2019, 6, 21),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 34,
        title = "Spider-Man: Far From Home",
        description="Following the events of Avengers: Endgame (2019), Peter Parker decides to take a vacation to Europe with his friends. However, his plans are put on hold when Nick Fury arrives needing his help to battle a new threat.",
        isLiked = false,
        createCalendar(2019, 7, 2),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 35,
        title = "It Chapter Two",
        description="Twenty-seven years after the events of It Chapter One (2017), the Losers' Club have grown up and moved away from Derry, Maine. However, they are all psychically linked by Pennywise the Dancing Clown, and they must reunite to stop him once and for all.",
        isLiked = false,
        createCalendar(2019, 9, 6),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 36,
        title = "Joker",
        description="In 1981, a struggling stand-up comedian named Arthur Fleck is mentally and emotionally broken by the indifference and cruelty he experiences every day. This mistreatment pushes him further into madness and leads him to become a violent criminal known as the Joker.",
        isLiked = false,
        createCalendar(2019, 10, 4),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 37,
        title = "Knives Out",
        description="A family gathering turns into a murder investigation when renowned crime novelist Harlan Thrombey is found dead at his estate.",
        isLiked = false,
        createCalendar(2019, 11, 27),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 38,
        title = "Jojo Rabbit",
        description="A young boy in Nazi Germany during World War II has an imaginary friend who is Hitler. He begins to question his beliefs when his widowed mother hides a young Jewish girl in their attic.",
        isLiked = false,
        createCalendar(2019, 10, 18),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 39,
        title = "Ford v Ferrari",
        description="American car designer Carroll Shelby and his British driver, Ken Miles, battle corporate interference and their own egos to build a revolutionary race car for Ford in order to defeat Ferrari at the 24 Hours of Le Mans in 1966.",
        isLiked = false,
        createCalendar(2019, 11, 15),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 40,
        title = "A Beautiful Day in the Neighborhood",
        description="A cynical journalist tasked with profiling Mister Rogers must confront his own childhood trauma while learning about empathy kindness and forgiveness.",
        isLiked = false,
        createCalendar(2019, 11, 22),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 41,
        title = "The Lighthouse",
        description="Two lighthouse keepers on a remote New England island in the late 19th century slowly descend into madness.",
        isLiked = false,
        createCalendar(2019, 10, 18),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 42,
        title = "Little Women",
        description="Jo March, Meg March, Amy March, and Beth March are four sisters who come of age in America during the Civil War.",
        isLiked = false,
        createCalendar(2019, 12, 25),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 43,
        title = "Marriage Story",
        description="A married couple navigates a contentious divorce.",
        isLiked = false,
        createCalendar(2019, 11, 6),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 44,
        title = "Clemency",
        description="A prison warden must confront the psychological toll of her job when she is tasked with overseeing the execution of a death row inmate.",
        isLiked = false,
        createCalendar(2019, 1, 27),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 45,
        title = "The Farewell",
        description="Billi, a young Chinese-American writer, travels to China to visit her ailing grandmother. What starts as an emotional reunion turns into an unexpected journey as Billi grapples with cultural differences and family secrets.",
        isLiked = false,
        createCalendar(2019, 4, 26),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 46,
        title = "Portrait of a Lady on Fire",
        description="A young woman in 18th-century France is unwillingly sent to a nunnery and awaits being married to a stranger. During this time, she develops a forbidden romance with a female painter who is sent to paint her portrait.",
        isLiked = false,
        createCalendar(2019, 5, 15),
        rating = 7.66f,
        voteCount = 100
    ),
    Movie(
        id = 47,
        title = "Pain and Glory",
        description="A fading Spanish filmmaker reflects on his life and career as he struggles with illness.",
        isLiked = false,
        createCalendar(2019, 5, 18),
        rating = 7.66f,
        voteCount = 100
    )
)