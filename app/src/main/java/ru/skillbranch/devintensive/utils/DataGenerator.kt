package ru.skillbranch.devintensive.utils

import ru.skillbranch.devintensive.extensions.TimeUnits
import ru.skillbranch.devintensive.extensions.add
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User
import java.util.*
import kotlin.random.Random

object DataGenerator {
    private val maleNames = listOf(
        "Robin",
        "James",
        "John",
        "Robert",
        "Michael",
        "William",
        "David",
        "Richard",
        "Charles",
        "Joseph",
        "Thomas",
        "Christopher",
        "Daniel",
        "Paul",
        "Mark",
        "Donald",
        "George",
        "Kenneth",
        "Steven",
        "Edward",
        "Brian",
        "Ronald",
        "Anthony",
        "Kevin",
        "Jason",
        "Matthew",
        "Gary",
        "Timothy",
        "Jose",
        "Larry",
        "Jeffrey",
        "Frank",
        "Scott",
        "Eric",
        "Stephen",
        "Andrew",
        "Raymond",
        "Gregory",
        "Joshua",
        "Jerry",
        "Dennis",
        "Walter",
        "Patrick",
        "Peter",
        "Harold",
        "Douglas",
        "Henry",
        "Carl",
        "Arthur",
        "Ryan",
        "Roger",
        "Joe",
        "Juan",
        "Jack",
        "Albert",
        "Jonathan",
        "Justin",
        "Terry",
        "Gerald",
        "Keith",
        "Samuel",
        "Willie",
        "Ralph",
        "Lawrence",
        "Nicholas",
        "Roy",
        "Benjamin",
        "Bruce",
        "Brandon",
        "Adam",
        "Harry",
        "Fred",
        "Wayne",
        "Billy",
        "Steve",
        "Louis",
        "Jeremy",
        "Aaron",
        "Randy",
        "Howard",
        "Eugene",
        "Carlos",
        "Russell",
        "Bobby",
        "Victor",
        "Martin",
        "Ernest",
        "Phillip",
        "Todd",
        "Jesse",
        "Craig",
        "Alan",
        "Shawn",
        "Clarence",
        "Sean",
        "Philip",
        "Chris",
        "Johnny",
        "Earl",
        "Jimmy",
        "Antonio"
    )
    private val femaleNames = listOf(
        "Mary",
        "Patricia",
        "Linda",
        "Barbara",
        "Elizabeth",
        "Jennifer",
        "Maria",
        "Susan",
        "Margaret",
        "Dorothy",
        "Lisa",
        "Nancy",
        "Karen",
        "Betty",
        "Helen",
        "Sandra",
        "Donna",
        "Carol",
        "Ruth",
        "Sharon",
        "Michelle",
        "Laura",
        "Sarah",
        "Kimberly",
        "Deborah",
        "Jessica",
        "Shirley",
        "Cynthia",
        "Angela",
        "Melissa",
        "Brenda",
        "Amy",
        "Anna",
        "Rebecca",
        "Virginia",
        "Kathleen",
        "Pamela",
        "Martha",
        "Debra",
        "Amanda",
        "Stephanie",
        "Carolyn",
        "Christine",
        "Marie",
        "Janet",
        "Catherine",
        "Frances",
        "Ann",
        "Joyce",
        "Diane",
        "Alice",
        "Julie",
        "Heather",
        "Teresa",
        "Doris",
        "Gloria",
        "Evelyn",
        "Jean",
        "Cheryl",
        "Mildred",
        "Katherine",
        "Joan",
        "Ashley",
        "Judith",
        "Rose",
        "Janice",
        "Kelly",
        "Nicole",
        "Judy",
        "Christina",
        "Kathy",
        "Theresa",
        "Beverly",
        "Denise",
        "Tammy",
        "Irene",
        "Jane",
        "Lori",
        "Rachel",
        "Marilyn",
        "Andrea",
        "Kathryn",
        "Louise",
        "Sara",
        "Anne",
        "Jacqueline",
        "Wanda",
        "Bonnie",
        "Julia",
        "Ruby",
        "Lois",
        "Tina",
        "Phyllis",
        "Norma",
        "Paula",
        "Diana",
        "Annie",
        "Lillian",
        "Emily"
    )
    private val lastNames = listOf(
        "Smith",
        "Johnson",
        "Williams",
        "Jones",
        "Brown",
        "Davis",
        "Miller",
        "Wilson",
        "Moore",
        "Taylor",
        "Anderson",
        "Thomas",
        "Jackson",
        "White",
        "Harris",
        "Martin",
        "Thompson",
        "Garcia",
        "Martinez",
        "Robinson",
        "Clark",
        "Rodriguez",
        "Lewis",
        "Lee",
        "Walker",
        "Hall",
        "Allen",
        "Young",
        "Hernandez",
        "King",
        "Wright",
        "Lopez",
        "Hill",
        "Scott",
        "Green",
        "Adams",
        "Baker",
        "Gonzalez",
        "Nelson",
        "Carter",
        "Mitchell",
        "Perez",
        "Roberts",
        "Turner",
        "Phillips",
        "Campbell",
        "Parker",
        "Evans",
        "Edwards",
        "Collins",
        "Stewart",
        "Sanchez",
        "Morris",
        "Rogers",
        "Reed",
        "Cook",
        "Morgan",
        "Bell",
        "Murphy",
        "Bailey",
        "Rivera",
        "Cooper",
        "Richardson",
        "Cox",
        "Howard",
        "Ward",
        "Torres",
        "Peterson",
        "Gray",
        "Ramirez",
        "James",
        "Watson",
        "Brooks",
        "Kelly",
        "Sanders",
        "Price",
        "Bennett",
        "Wood",
        "Barnes",
        "Ross",
        "Henderson",
        "Coleman",
        "Jenkins",
        "Perry",
        "Powell",
        "Long",
        "Patterson",
        "Hughes",
        "Flores",
        "Washington",
        "Butler",
        "Simmons",
        "Foster",
        "Gonzales",
        "Bryant",
        "Alexander",
        "Russell",
        "Griffin",
        "Diaz",
        "Hayes"
    )
    private val maleAvatars = listOf(
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man0.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man1.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man2.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man3.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man4.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man5.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man6.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man7.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man8.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/man9.png"
    )
    private val femaleAvatars = listOf(
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman0.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman1.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman2.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman3.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman4.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman5.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman6.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman7.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman8.png",
        "https://skill-branch.ru/resources/dev-intensive-2019/avatars/woman9.png"
    )

    val stabUsers = generateUsers(20)
    val stabChats = generateStabChats(10)

    private fun generateUsers(count: Int): List<User> {
        val users = mutableListOf<User>()
        for (i in 0 until count) {
            val isMale: Boolean = Random.nextBoolean()
            val (lastVisit: Date?, isOnline: Boolean) = randomDateAndOnline()
            users.add(
                User(
                    "$i",
                    if (isMale) maleNames.random() else femaleNames.random(),
                    lastNames.random(true),
                    if (isMale) maleAvatars.random(true) else femaleAvatars.random(true),
                    (0..Short.MAX_VALUE).random(),
                    (0..Short.MAX_VALUE).random(),
                    lastVisit,
                    isOnline
                )
            )
        }
        return users
    }

    private fun generateStabChats(count: Int): List<Chat> {
        val list = mutableListOf<Chat>()
        /** Для каждого юзера сгенерим персональный сингл-чат и добавим в список */
        for (user in stabUsers) {
            val singleChat = Chat(
                "${list.size}",
                "",
                listOf(user)
            )
            singleChat.messages = generateRandomMessages(singleChat, listOf(user))
            list.add(singleChat)
        }
        /** Дополним список групповыми чатами */
        for (i in 0 until count) {
            val members = stabUsers.randomSublist(5, 2)
            val groupChat = Chat(
                "${list.size}",
                members.map { it.firstName }.joinToString(", "),
                members
            )
            groupChat.messages = generateRandomMessages(groupChat, members)
            list.add(groupChat)
        }
        return list
    }

    fun generateChats(count: Int, hasGroups: Boolean = false): List<Chat> {
        val list = mutableListOf<Chat>()
        val users = generateUsers(count)
        for (user in users) {
            val singleChat = Chat(
                "${list.size}",
                "",
                listOf(user)
            )
            singleChat.messages = generateRandomMessages(singleChat, listOf(user))
            list.add(singleChat)
        }
        if (hasGroups) {
            for (i in 0..count) {
                val members = users.randomSublist(5, 2)
                val groupChat = Chat(
                    "${list.size}",
                    "Группа ${list.size}",
                    members
                )
                groupChat.messages = generateRandomMessages(groupChat, members)
                list.add(groupChat)
            }
        }

        return list
    }

    fun generateChatsWithOffset(startId: Int, count: Int): List<Chat> {
        val list = mutableListOf<Chat>()
        val users = generateUsers(count)
        for (user in users) {
            val singleChat = Chat(
                "${startId + list.size}",
                "",
                listOf(user)
            )
            singleChat.messages = generateRandomMessages(singleChat, listOf(user))
            list.add(singleChat)
        }
        return list
    }

    /** Получаем список упорядоченных по id (но не по дате) сообщений для ЧАТА */
    private fun generateRandomMessages(chat: Chat, users: List<User>)
            : MutableList<BaseMessage> {
        val list = mutableListOf<BaseMessage>()
        val rnd = (0..12).random()
        for (i in 0 until rnd) {
            val user = randomUser(users)
            // Short.MAX_VALUE=~32000, 32 тыс. минут->~533ч->~22д
            val offset = (0..Short.MAX_VALUE).random()

            /** Если юзер сейчас не в чате, то дата исходящего от НЕГО сообщения,
             * не может быть позже его последнего визита в чат, а дата этого
             * визита не может быть null */
            val date = when {
                user.isOnline -> Date().add(-offset, TimeUnits.SECOND)
                // Костыль для генератора на стороне тестов (логика нарушена)
                else -> user.lastVisit?.add(-offset, TimeUnits.MINUTE) ?: Date()
            }
            list.add(
                // Айди сообщений возрастают по порядку В ЧАТЕ. Если чат
                // групповой, то разным юзерам достанутся сообщения со случайными
                // неупорядочными айди
                TextMessage(
                    "$i",
                    user, // сообщение в чат написал этот юзер (параметр from)
                    chat,
                    // Все сообщения пока что созданы генератором, а не юзером,
                    // который открыл данное приложение на своем телефоне и увидел
                    // список чатов с сообщениями. Следовательно они все входящие
                    true,
                    date,
                    false,
                    randomTextPayload()
                )
            )
        }
        return list
    }

    private fun randomTextPayload(): String {
        val lorem =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vel nulla lectus. Phasellus et ullamcorper quam. Nunc sollicitudin viverra lacus. Mauris pellentesque sodales gravida. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Proin lacinia magna erat, in elementum leo laoreet a. Curabitur eleifend sapien ut augue dapibus, et luctus nisi aliquam. Pellentesque iaculis aliquam dolor. In accumsan ex non ante vulputate, id bibendum elit congue. Ut eget dictum nibh. Vestibulum accumsan interdum velit et volutpat. Aliquam nec consequat nibh. Vivamus et dignissim arcu, eget elementum lacus. Aliquam id metus quis velit aliquet elementum at non ligula. Vivamus non pulvinar ligula, ac dignissim leo.Donec gravida nunc quis ex cursus, sit amet fringilla arcu varius. Cras porta id nunc nec pretium. Mauris aliquet dolor non arcu rutrum, in pretium magna vestibulum. Phasellus venenatis odio sit amet dictum pellentesque. Duis sed augue vestibulum, pulvinar arcu vel, bibendum mauris. Aenean congue eleifend magna in suscipit. Donec a tellus in tellus pellentesque sollicitudin. Fusce aliquam lacus sit amet neque sollicitudin semper.Aliquam faucibus tristique tempor. Maecenas non purus a dolor sodales tempus. Praesent nec nibh in orci volutpat malesuada a sed ligula. Quisque tempus ipsum ex, eget vehicula urna tincidunt a. Maecenas maximus vel nibh id luctus. Vivamus condimentum velit vitae ante pellentesque, quis fringilla velit vestibulum. Curabitur at malesuada augue. Ut sapien enim, lobortis sed quam ut, hendrerit euismod metus. Ut vel lectus eget ante tempor blandit. In vitae nisi eu dolor consectetur elementum nec id purus.".split(
                ". "
            )

        val phrasesCount = (1..3).random()
        return lorem.randomSublist(phrasesCount).joinToString(" ")
    }

    private fun randomUser(users: List<User>): User {
        val rndInd = (users.indices).random()
        return users[rndInd]
    }

    /** Получаем случайный элемент из списка. Если передан аргумент true,
     * то получаем либо null, либо случайный элемент с вероятностью 50/50 */
    private fun <T> List<T>.random(includeNull: Boolean = false): T? {
        return if (includeNull && Random.nextBoolean()) null
        else this[(this.indices).random()]
    }

    private fun <T> List<T>.randomSublist(maxCount: Int, minCount: Int = 1): List<T> {
        val rndCount = (minCount..maxCount).random()
        val ind = (0 until this.size - rndCount).random()
        return this.subList(ind, ind + rndCount)
    }

    /** Получаем пару - дату последнего визита юзера в символический чат
     * и флаг, онлайн юзер или нет (дата может быть null, если юзер онлайн) */
    private fun randomDateAndOnline(): Pair<Date?, Boolean> {
        val isNow = Random.nextBoolean()
        val isVisited = Random.nextBoolean()
        // Short.MAX_VALUE=~32000, 32 тыс. минут->~533ч->~22д
        val offset = (0..Short.MAX_VALUE * 3).random()
        val date = Date().add(-offset, TimeUnits.MINUTE)
        return when {
            // Половина из списка юзеров когда-то посещала чат и сейчас не в чате
            isVisited -> Pair(date, false)
            // Четверть из списка юзеров сейчас в чате, из них половина в чате
            // впервые (null), а другая половина уже бывала в чате
            isNow -> Pair(
                if (Random.nextBoolean()) null else
                    Date().add(-(1..27).random(), TimeUnits.HOUR), true
            )
            // Оставшаяся четверть из списка юзеров сейчас не в чате и раньше
            // в нем не бывали. Но такие юзеры бесполезны для символического чата,
            // поэтому их раскидаем между первыми двумя вариантами
            else -> if (Random.nextBoolean()) Pair(date, false)
            else Pair(
                if (Random.nextBoolean()) null else
                    Date().add(-(1..27).random(), TimeUnits.HOUR), true
            )
        }
    }
}