package co.uk.bbk.culinarycompanion
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class RecipesDaoTest {
    private lateinit var db: RecipesDatabase
    private lateinit var dao: RecipesDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.recipesDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertRecipe_shouldInsertCorrectly() = runBlocking {
        val recipe = Recipe(title = "Test Recipe", ingredients = "Eggs", instructions = "Boil", category = "Breakfast")
        dao.insertRecipe(recipe)

        val allRecipes = dao.getAllRecipes()
        assertEquals(1, allRecipes.size)
        assertEquals("Test Recipe", allRecipes[0].title)
    }


    @Test
    fun deleteRecipe_shouldRemoveFromDb() = runBlocking {
        val recipe = Recipe(title = "Delete Me", ingredients = "Toast", instructions = "Burn it", category = "Snack")
        dao.insertRecipe(recipe)

        val inserted = dao.getAllRecipes().first { it.title == "Delete Me" }

        dao.deleteRecipe(inserted)
        val all = dao.getAllRecipes()
        assertTrue(all.none { it.title == "Delete Me" })
    }
}