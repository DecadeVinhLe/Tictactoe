package vinh.le.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import vinh.le.tictactoe.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.playOfflineBtn.setOnClickListener {
            createOfflineGame()
        }
        binding.createOnlineGameBtn.setOnClickListener {
            
        }
        binding.joinOnlineGameBtn.setOnClickListener {
        
        }
    }
    fun createOfflineGame(){
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )
        startGame()
        
    }
    
    fun createOnlineGame() {
        GameData.myID = "X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameID = Random.nextInt(1000..9999).toString()
            )
        )
        startGame()
        
    }
    
    fun joinOnlineGame() {
        var gameID = binding.gameInputId.text.toString()
        if (gameID.isEmpty()) {
            binding.gameInputId.error = "Please Enter Game ID"
            return
        }
        GameData.myID = "O"
        Firebase.firestore.collection("games")
            .document(gameID)
            .get()
            .addOnSuccessListener {
                val model = it?.toObject(GameModel::class.java)
                if (model == null) {
                    binding.gameInputId.error = "Please Enter Valid Game ID"
                } else {
                    model.gameStatus = GameStatus.JOINED
                    GameData.saveGameModel(model)
                    startGame()
                }
            }
    }
    fun startGame(){
        startActivity(Intent(this,GameActivity::class.java))
        
    }
}
