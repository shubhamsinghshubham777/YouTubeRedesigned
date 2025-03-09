package com.google.youtube.data

import com.google.youtube.models.CollectionChannelItem
import com.google.youtube.models.CollectionData
import com.google.youtube.utils.Asset

class CollectionsDataProvider {
    fun getCollections(): List<CollectionData> {
        return listOf(
            CollectionData(
                name = "Gaming",
                channelItems = listOf(
                    CollectionChannelItem(
                        channelId = "ninja",
                        avatarAsset = Asset.Channel.NINJA,
                        channelName = "Ninja",
                        isVerified = true,
                        subscribersCount = "23.8M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "eveonline",
                        avatarAsset = Asset.Channel.EVE_ONLINE,
                        channelName = "Eve Online",
                        isVerified = true,
                        subscribersCount = "141K",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "howtobasic",
                        avatarAsset = Asset.Channel.HOW_TO_BASIC,
                        channelName = "HowToBasic",
                        isVerified = true,
                        subscribersCount = "17.6M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "cyberpunk2077",
                        avatarAsset = Asset.Channel.CYBERPUNK_2077,
                        channelName = "Cyberpunk 2077",
                        isVerified = true,
                        subscribersCount = "1.7M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "fortnite",
                        avatarAsset = Asset.Channel.FORTNITE,
                        channelName = "Fortnite",
                        isVerified = true,
                        subscribersCount = "12.9M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "thegametheorists",
                        avatarAsset = Asset.Channel.THE_GAME_THEORISTS,
                        channelName = "The Game Theorists",
                        isVerified = true,
                        subscribersCount = "19.4M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "gamespot",
                        avatarAsset = Asset.Channel.GAMESPOT,
                        channelName = "GameSpot",
                        isVerified = true,
                        subscribersCount = "5.6M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "playstation",
                        avatarAsset = Asset.Channel.PLAYSTATION,
                        channelName = "PlayStation",
                        isVerified = true,
                        subscribersCount = "16M",
                        isSubscribed = true,
                    ),
                ),
            ),
            CollectionData(
                name = "Design & Code",
                channelItems = listOf(
                    CollectionChannelItem(
                        channelId = "juxtopposed",
                        avatarAsset = Asset.Channel.JUXTOPPOSED,
                        channelName = "Juxtopposed",
                        isVerified = true,
                        subscribersCount = "295K",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "fireship",
                        avatarAsset = Asset.Channel.FIRESHIP,
                        channelName = "Fireship",
                        isVerified = true,
                        subscribersCount = "3.47M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "brandon_shepherd",
                        avatarAsset = Asset.Channel.BRANDON_SHEPHERD,
                        channelName = "Brandon Shepherd",
                        isVerified = true,
                        subscribersCount = "596K",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "coding_with_lewis",
                        avatarAsset = Asset.Channel.CODING_WITH_LEWIS,
                        channelName = "Coding with Lewis",
                        isVerified = true,
                        subscribersCount = "622K",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "daniel_krafft",
                        avatarAsset = Asset.Channel.DANIEL_KRAFFT,
                        channelName = "Daniel Krafft",
                        isVerified = true,
                        subscribersCount = "2.28M",
                        isSubscribed = true,
                    ),
                ),
            ),
            CollectionData(
                name = "Tech",
                channelItems = listOf(
                    CollectionChannelItem(
                        channelId = "bog",
                        avatarAsset = Asset.Channel.BOG,
                        channelName = "BOG",
                        isVerified = true,
                        subscribersCount = "193K",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "vsauce",
                        avatarAsset = Asset.Channel.VSAUCE,
                        channelName = "Vsauce",
                        isVerified = true,
                        subscribersCount = "23.3M",
                        isSubscribed = true,
                    ),
                    CollectionChannelItem(
                        channelId = "the_reporter_of_the_week",
                        avatarAsset = Asset.Channel.THE_REPORT_OF_THE_WEEK,
                        channelName = "TheReportOfTheWeek",
                        isVerified = true,
                        subscribersCount = "2.86M",
                        isSubscribed = true,
                    ),
                ),
            ),
        )
    }
}
