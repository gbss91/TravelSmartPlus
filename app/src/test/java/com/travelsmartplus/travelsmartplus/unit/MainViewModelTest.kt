package com.travelsmartplus.travelsmartplus.unit

import MainDispatcherRule
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val sessionManager: SessionManager = mockk()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { sessionManager.currentUser() } returns 1
        every { sessionManager.isSetup() } returns false
        viewModel = MainViewModel(sessionManager)

    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentUser should return user`() {

        every { sessionManager.currentUser() } returns 1

        val currentUser = viewModel.getCurrentUser()
        assertEquals(1, currentUser)
    }

    @Test
    fun `test isAdmin true`() {

        every { sessionManager.admin() } returns true

        val isAdmin = viewModel.isAdmin()
        assertEquals(true, isAdmin)
    }

    @Test
    fun `test isAdmin false`() {

        every { sessionManager.admin() } returns false

        val isAdmin = viewModel.isAdmin()
        assertEquals(false, isAdmin)
    }

}