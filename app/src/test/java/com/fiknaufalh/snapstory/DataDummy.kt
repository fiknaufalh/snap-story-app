package com.fiknaufalh.snapstory

import com.fiknaufalh.snapstory.data.remote.responses.StoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<StoryItem> {
        val items: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItem(
                i.toString(),
                "https://story.com/$i.jpg",
                "June ${i%30}, 2024",
                "Story $i",
                "Lorem $i ipsum dolor sit amet ${i+5}.",
                i.toDouble(),
                (i+1).toDouble()
            )
            items.add(story)
        }
        return items
    }
}