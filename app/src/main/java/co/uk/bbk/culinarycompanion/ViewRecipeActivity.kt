package co.uk.bbk.culinarycompanion

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import co.uk.bbk.culinarycompanion.databinding.ViewRecipeBinding
import kotlinx.coroutines.launch

class ViewRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ViewRecipeBinding

    private val editRecipeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedRecipe = result.data?.getSerializableExtra("recipe", Recipe::class.java)
            if (updatedRecipe != null) {
                binding.recipe = updatedRecipe
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.view_recipe)

        val recipe = intent.getSerializableExtra("recipe", Recipe::class.java)
        if (recipe != null) {
            binding.recipe = recipe

            // Edit button logic, navigates to EditRecipeActivity when clicked
            binding.buttonEdit.setOnClickListener {
                val editIntent = Intent(this, EditRecipeActivity::class.java)
                editIntent.putExtra("recipe", binding.recipe)
                editRecipeLauncher.launch(editIntent)
            }

            //Delete button logic, deletes recipe from database
            binding.buttonDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Yes") { _, _ ->
                        lifecycleScope.launch {
                            val dao = RecipesDatabase.getInstance(applicationContext).recipesDao()
                            dao.deleteRecipe(recipe)
                            finish() // Close the activity after deletion
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        } else {
            finish()
        }
    }
}