package com.example.storyapp_androidintermediate_sub1.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp_androidintermediate_sub1.data.api.ApiService
import com.example.storyapp_androidintermediate_sub1.retrofit.response.ListStoryItem

class StoryPagingSource (private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position, params.loadSize)
            val stories = responseData.listStory
            val storiesList = stories.map {
                ListStoryItem(
                    it?.photoUrl,
                    it?.createdAt,
                    it?.name,
                    it?.description,
                    it?.lon,
                    it!!.id,
                    it?.lat
                )
            }
            LoadResult.Page(
                data = storiesList,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (storiesList.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}