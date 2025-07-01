package co.uk.bbk.culinarycompanion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MainViewModel
    private lateinit var mockDao: RecipesDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockDao = mockk()
        viewModel = MainViewModel().apply {
            recipesDao = mockDao
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `filterRecipesByCategory should update LiveData with filtered recipes`() = runTest {
        val mockList = listOf(
            Recipe(1, "Salad", "Lunch", "Lettuce", "Chop and mix"),
            Recipe(2, "Soup", "Lunch", "Tomato", "Boil and stir")
        )
        coEvery { mockDao.getRecipesByCategory("Lunch") } returns mockList

        viewModel.filterRecipesByCategory("Lunch")
        advanceUntilIdle()

        val result = viewModel.recipes.value
        assertEquals(2, result?.size)
        assertEquals("Salad", result?.get(0)?.title)
    }
}