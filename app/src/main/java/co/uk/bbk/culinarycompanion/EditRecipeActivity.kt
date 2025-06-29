package co.uk.bbk.culinarycompanion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import co.uk.bbk.culinarycompanion.databinding.EditRecipeBinding
import kotlinx.coroutines.launch

class EditRecipeActivity : AppCompatActivity() {

    private lateinit var binding: EditRecipeBinding
    private lateinit var dao: RecipesDao
    private var recipeId: Long? = null // For editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = RecipesDatabase.getInstance(applicationContext).recipesDao()

        // Check if editing an existing recipe to prefill recipe details
        val recipe = intent.getSerializableExtra("recipe", Recipe::class.java)
        if (recipe != null) {
            recipeId = recipe.id
            binding.editTitle.setText(recipe.title)
            binding.editIngredients.setText(recipe.ingredients)
            binding.editInstructions.setText(recipe.instructions)

            // Set existing spinner category
            val categories = resources.getStringArray(R.array.recipe_categories)
            val index = categories.indexOf(recipe.category)
            if (index >= 0) binding.spinnerCategory.setSelection(index)
        }

        // Save button logic to save recipe details to database
        binding.buttonSave.setOnClickListener {
            val title = binding.editTitle.text.toString().trim()
            val category = binding.spinnerCategory.selectedItem.toString()
            val ingredients = binding.editIngredients.text.toString().trim()
            val instructions = binding.editInstructions.text.toString().trim()

            // Validation to ensure title, ingredients and instructions are not empty
            if (title.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create or update recipe
            val newRecipe = Recipe(
                id = recipeId ?: 0,
                title = title,
                category = category,
                ingredients = ingredients,
                instructions = instructions
            )

            // Updates database with either updated details or new recipe
            // lifecycleScope.launch ensure to cancel task if activity is destroyed
            lifecycleScope.launch {
                if (recipeId != null) {
                    dao.updateRecipe(newRecipe)
                } else {
                    dao.insertRecipe(newRecipe)
                }
                finish()
            }
        }
    }
}