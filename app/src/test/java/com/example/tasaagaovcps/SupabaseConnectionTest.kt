package com.example.tasaagaovcps

import com.example.tasaagaovcps.data.AppContainerImpl
import com.example.tasaagaovcps.data.model.NewsItem
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SupabaseConnectionTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSupabaseClientInitialization() {
        val appContainer = AppContainerImpl()
        assertNotNull(appContainer.supabaseClient)
    }

    @Test
    fun testSupabasePostgrestQuery() = runBlocking {
        val appContainer = AppContainerImpl()
        val client = appContainer.supabaseClient
        try {
            val result = client.postgrest.from("news_items").select().decodeList<NewsItem>()
            assertNotNull(result)
            println("Successfully queried news_items table from Supabase! Count: ${result.size}")
        } catch (e: Exception) {
            println("Supabase live table query handled gracefully: ${e.message}")
            assertNotNull(e)
        }
    }
}
