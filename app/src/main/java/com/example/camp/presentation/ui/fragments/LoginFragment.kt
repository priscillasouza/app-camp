package com.example.camp.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.camp.databinding.FragmentLoginBinding
import com.example.camp.presentation.viewmodel.LoginViewModel
import com.example.camp.util.ViewState

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    //faz a injeção de dependência do viewModel
    private val viewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        addObserver()
    }

    private fun setListener() {
        binding.buttonEnter.setOnClickListener {
                binding.run {
                    viewModel.login(
                        textEditEmail.text.toString(),
                        textEditPassword.text.toString()
                    )

                    textEditEmail.addTextChangedListener {
                        textError.visibility = View.GONE
                    }
                    textEditPassword.addTextChangedListener {
                        textPassword.visibility = View.GONE
                    }

                }
        }
    }

    //observa o liveData que está na viewModel
    private fun addObserver() {
        viewModel.loggedUserViewState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is ViewState.Success -> {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToBookListFragment()
                    )
                }
                is ViewState.Error -> {
                    binding.progressDialog.visibility = View.GONE
                    binding.textError.visibility = View.VISIBLE
                }
                is ViewState.Loading -> {
                    binding.progressDialog.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetViewState()
        _binding = null
    }
}