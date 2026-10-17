package com.example.vaultlab

import android.content.ContextWrapper
import android.content.SharedPreferences
import com.example.vaultlab.data.AuthResult
import com.example.vaultlab.data.SyntheticRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Proxy

class SyntheticRepositoryTest {

    private lateinit var repository: SyntheticRepository
    private val memoryStore = mutableMapOf<String, Any>()

    @Before
    fun setUp() {
        memoryStore.clear()

        var editorProxy: SharedPreferences.Editor? = null
        editorProxy = Proxy.newProxyInstance(
            SharedPreferences.Editor::class.java.classLoader,
            arrayOf(SharedPreferences.Editor::class.java)
        ) { _, method, args ->
            when (method.name) {
                "putBoolean" -> {
                    memoryStore[args[0] as String] = args[1] as Boolean
                    editorProxy
                }
                "putString" -> {
                    memoryStore[args[0] as String] = args[1] as String
                    editorProxy
                }
                "clear" -> {
                    memoryStore.clear()
                    editorProxy
                }
                "apply", "commit" -> editorProxy
                else -> editorProxy
            }
        } as SharedPreferences.Editor

        val prefsProxy = Proxy.newProxyInstance(
            SharedPreferences::class.java.classLoader,
            arrayOf(SharedPreferences::class.java)
        ) { _, method, args ->
            when (method.name) {
                "getBoolean" -> memoryStore[args[0] as String] as? Boolean ?: (args[1] as Boolean)
                "getString" -> memoryStore[args[0] as String] as? String ?: (args[1] as String)
                "edit" -> editorProxy
                else -> null
            }
        } as SharedPreferences

        val fakeContext = object : ContextWrapper(null) {
            override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
                return prefsProxy
            }
        }

        repository = SyntheticRepository(fakeContext)
    }

    @Test
    fun testUser1LoginSuccess() {
        val result = repository.authenticate("user1", "password123")
        assertEquals(AuthResult.SUCCESS_STANDARD, result)
        assertTrue(repository.isLoggedIn())
        assertEquals("user1", repository.getCurrentUser().username)
    }

    @Test
    fun testAdminLoginSuccess() {
        val result = repository.authenticate("admin", "VaultAdmin2025Secret!")
        assertEquals(AuthResult.SUCCESS_ADMIN, result)
        assertTrue(repository.isLoggedIn())
        assertEquals("admin", repository.getCurrentUser().username)
    }

    @Test
    fun testWrongPasswordFails() {
        val result = repository.authenticate("admin", "wrongpass")
        assertEquals(AuthResult.FAILED, result)
        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun testEmptyFieldsFail() {
        assertEquals(AuthResult.FAILED, repository.authenticate("", ""))
        assertEquals(AuthResult.FAILED, repository.authenticate("admin", ""))
        assertEquals(AuthResult.FAILED, repository.authenticate("", "VaultAdmin2025Secret!"))
        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun testLogoutClearsSession() {
        repository.authenticate("user1", "password123")
        assertTrue(repository.isLoggedIn())

        repository.logout()
        assertFalse(repository.isLoggedIn())
    }
}
