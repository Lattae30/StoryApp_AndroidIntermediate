package com.example.storyapp_androidintermediate_sub1

import com.example.storyapp_androidintermediate_sub1.retrofit.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val lat = i.toDouble()
            val lon = i.toDouble()

            val story = ListStoryItem(
                i.toString(),
                "createdAt + $i",
                "name + $i",
                "description + $i",
                lon.toDouble(),
                "id + $i",
                lat.toDouble()
            )
            items.add(story)
        }
        return items
    }
}