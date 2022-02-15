package com.example.myapplication.view.registrationView

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.isGone
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.example.myapplication.common.RegistrationContract
import com.example.myapplication.databinding.FragmentRegistrationBinding
import com.example.myapplication.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity(), RegistrationContract {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var auth: FirebaseAuth

    private var fragStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val motionContainer = binding.registrationLay

        auth = Firebase.auth
        if (auth.currentUser != null) {
            toMainActivity()
        } else {

            binding.registrationLay.setTransitionListener(object : TransitionAdapter() {
                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    when (currentId) {
                        R.id.endSignIn -> toSignInFun()
                        R.id.endSignUp -> toSignUpFun()
                    }
                }
            })

            binding.registrationButtonSignIn.setOnClickListener {
                motionContainer.setTransition(R.id.startSignIn, R.id.endSignIn)
                motionContainer.transitionToEnd()

                fragStatus = true
            }

            binding.registrationButtonSignUp.setOnClickListener {
                motionContainer.setTransition(R.id.startSignUp, R.id.endSignUp)
                motionContainer.transitionToEnd()

                fragStatus = true
            }
        }
    }


    override fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun toSignInCard() {
        toSignInFun()
    }

    private fun toSignInFun() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.registrationSignInCard, SignInFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun toSignUpCard() {
        toSignUpFun()
    }

    private fun toSignUpFun() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.registrationSignUpCard, SignUpFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun fromSignInToSignUp() {
        binding.registrationLay.setTransition(R.id.endSignIn, R.id.endSignUp)
        binding.registrationLay.transitionToEnd()
    }

    override fun fromSignUpToSignIn() {
        binding.registrationLay.setTransition(R.id.endSignUp, R.id.endSignIn)
        binding.registrationLay.transitionToEnd()
    }

    override fun onBackPressed() {
        if (fragStatus){
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)

            finish()
        } else{
            super.onBackPressed()
        }
    }
}
