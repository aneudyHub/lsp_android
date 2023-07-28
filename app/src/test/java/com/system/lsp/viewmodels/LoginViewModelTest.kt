package com.system.lsp.viewmodels

import com.lsp.logger.LogFactory
import com.system.lsp.R
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.UsersRepository
import com.system.lsp.domain.SignInUseCase
import com.system.lsp.ui.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class LoginViewModelTest {

    private lateinit var signInUseCaseMock: SignInUseCase
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var usersRepositoryMock: UsersRepository

    private lateinit var coroutineScope: CoroutineScope

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        coroutineScope = CoroutineScope(testDispatcher)
        signInUseCaseMock = SignInUseCase(usersRepositoryMock)
        viewModel = LoginViewModel(signInUseCaseMock, mock(LogFactory::class.java))


    }

    @After
    fun teardown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Sign in should success`() = testDispatcher.runBlockingTest {
        val userName = "user"
        val password = "password"
        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(Result.Success(null))
        viewModel.signIn(userName, password)
        val uiState = viewModel.uiState.first()
        Assert.assertEquals(LoginViewModel.UiState.Success, uiState)
    }

    @Test
    fun `User and Password should not be empty`() = testDispatcher.runBlockingTest {
        var userName = ""
        var password = ""

        viewModel.signIn(userName, password)
        var uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.user_password_should_be_not_empty),
            uiState
        )

        userName = "User"
        password = ""

        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.user_password_should_be_not_empty),
            uiState
        )

        userName = ""
        password = "Password"

        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.user_password_should_be_not_empty),
            uiState
        )


        userName = "User"
        password = "Password"

        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(Result.Success(null))
        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(LoginViewModel.UiState.Success, uiState)

    }

    @Test
    fun `Sign in got a network error`() = testDispatcher.runBlockingTest {
        val userName = "user"
        val password = "password"
        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(
            Result.Error(
                HttpResponseErrorCode.BAD_REQUEST
            )
        )

        viewModel.signIn(userName, password)
        var uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.api_error_user_pass_are_bad),
            uiState
        )


        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(
            Result.Error(
                HttpResponseErrorCode.UNAUTHORIZED
            )
        )

        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.api_error_user_pass_are_bad),
            uiState
        )

        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(
            Result.Error(
                HttpResponseErrorCode.FORBIDDEN
            )
        )

        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.api_error_user_pass_are_bad),
            uiState
        )



        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(
            Result.Error(
                HttpResponseErrorCode.NOT_FOUND
            )
        )

        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.api_error_server_outage),
            uiState
        )


        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(
            Result.Error(
                HttpResponseErrorCode.INTERNAL_SERVER_ERROR
            )
        )

        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(
            LoginViewModel.UiState.Failed(R.string.api_error_server_outage),
            uiState
        )


        `when`(usersRepositoryMock.signIn(userName, password)).thenReturn(
            Result.Error(
                HttpResponseErrorCode.UNKNOWN
            )
        )

        viewModel.signIn(userName, password)
        uiState = viewModel.uiState.first()
        Assert.assertEquals(LoginViewModel.UiState.Failed(R.string.thrown_exception), uiState)
    }
}