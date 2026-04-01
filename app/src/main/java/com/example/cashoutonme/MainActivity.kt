package com.example.cashoutonme

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashoutonme.ui.theme.CashoutOnMeTheme

enum class Screen {
    Login, Register, TopUp
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashoutOnMeTheme {
                var currentScreen by remember { mutableStateOf(Screen.Login) }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        @OptIn(ExperimentalMaterial3Api::class)
                        CenterAlignedTopAppBar(
                            title = { 
                                Text(
                                    when(currentScreen) {
                                        Screen.Login -> "Login Kamael"
                                        Screen.Register -> "Register Kamael"
                                        Screen.TopUp -> "Kamael Store TopUp"
                                    }, 
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                ) 
                            },
                            navigationIcon = {
                                if (currentScreen != Screen.Login) {
                                    IconButton(onClick = { 
                                        currentScreen = Screen.Login 
                                    }) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.Login -> LoginScreen(
                                onLoginSuccess = { currentScreen = Screen.TopUp },
                                onNavigateToRegister = { currentScreen = Screen.Register }
                            )
                            Screen.Register -> RegisterScreen(
                                onRegisterSuccess = { currentScreen = Screen.Login }
                            )
                            Screen.TopUp -> TopUpForm(
                                onLogout = { currentScreen = Screen.Login }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.isNotEmpty()
    val canLogin = isEmailValid && isPasswordValid

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Selamat Datang!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = email, 
            onValueChange = { email = it },
            label = { Text("Email") }, 
            modifier = Modifier.fillMaxWidth(),
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = {
                if (email.isNotEmpty() && !isEmailValid) {
                    Text("Format email tidak valid")
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password, 
            onValueChange = { password = it },
            label = { Text("Password") }, 
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isEmpty() && email.isNotEmpty(),
            supportingText = {
                if (password.isEmpty() && email.isNotEmpty()) {
                    Text("Password tidak boleh kosong")
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = canLogin
        ) {
            Text("LOGIN")
        }
        
        TextButton(onClick = onNavigateToRegister) {
            Text("Belum punya akun? Daftar di sini")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    val hobbies = remember { mutableStateListOf<String>() }
    var selectedUserType by remember { mutableStateOf("") }
    
    var showResetDialog by remember { mutableStateOf(false) }
    var showConfirmSubmitDialog by remember { mutableStateOf(false) }

    // Real-time Validation Logic
    val isNameValid = name.isNotBlank()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6
    val isPasswordMatch = password == confirmPassword && confirmPassword.isNotEmpty()
    val isGenderValid = gender.isNotEmpty()
    val isHobbiesValid = hobbies.size >= 3
    val isUserTypeValid = selectedUserType.isNotEmpty()
    
    val canSubmit = isNameValid && isEmailValid && isPasswordValid && isPasswordMatch && 
                    isGenderValid && isHobbiesValid && isUserTypeValid

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registrasi Akun Kamael", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        
        OutlinedTextField(
            value = name, 
            onValueChange = { name = it }, 
            label = { Text("Nama Lengkap") }, 
            modifier = Modifier.fillMaxWidth(),
            isError = name.isNotEmpty() && !isNameValid,
            supportingText = { if (name.isNotEmpty() && !isNameValid) Text("Nama tidak boleh kosong") }
        )

        OutlinedTextField(
            value = email, 
            onValueChange = { email = it }, 
            label = { Text("Email") }, 
            modifier = Modifier.fillMaxWidth(),
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = { if (email.isNotEmpty() && !isEmailValid) Text("Format email tidak valid") }
        )

        OutlinedTextField(
            value = password, 
            onValueChange = { password = it }, 
            label = { Text("Password (Min 6)") }, 
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isNotEmpty() && !isPasswordValid,
            supportingText = { if (password.isNotEmpty() && !isPasswordValid) Text("Minimal 6 karakter") }
        )

        OutlinedTextField(
            value = confirmPassword, 
            onValueChange = { confirmPassword = it }, 
            label = { Text("Konfirmasi Password") }, 
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPassword.isNotEmpty() && !isPasswordMatch,
            supportingText = { if (confirmPassword.isNotEmpty() && !isPasswordMatch) Text("Password tidak cocok") }
        )

        Text("Jenis Kelamin", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = gender == "L", onClick = { gender = "L" })
            Text("Laki-laki")
            Spacer(Modifier.width(8.dp))
            RadioButton(selected = gender == "P", onClick = { gender = "P" })
            Text("Perempuan")
        }

        Text("Hobi (Pilih Min 3)", fontWeight = FontWeight.Bold)
        val hobbyOptions = listOf("Gaming", "Coding", "Streaming", "Membaca", "Musik", "Traveling")
        hobbyOptions.forEach { hobby ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hobbies.contains(hobby), onCheckedChange = { if(it) hobbies.add(hobby) else hobbies.remove(hobby) })
                Text(hobby)
            }
        }
        if (!isHobbiesValid && hobbies.isNotEmpty()) {
            Text("Pilih minimal 3 hobi (Terpilih: ${hobbies.size})", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        // TUGAS: Spinner (Dropdown) data custom diperbarui
        Text("Tipe Akun", fontWeight = FontWeight.Bold)
        var expanded by remember { mutableStateOf(false) }
        val userTypes = listOf("Gamer", "Reseller", "Member")
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedUserType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Tipe Akun") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                userTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedUserType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        // Tombol Daftar dengan Long Press
        val interactionSource = remember { MutableInteractionSource() }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = canSubmit,
                    onClick = { if (canSubmit) showConfirmSubmitDialog = true },
                    onLongClick = { showResetDialog = true }
                ),
            shape = RoundedCornerShape(100.dp),
            color = if (canSubmit) MaterialTheme.colorScheme.primary else Color.Gray,
            contentColor = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("DAFTAR SEKARANG", fontWeight = FontWeight.Bold)
            }
        }
        
        if (!canSubmit) {
            Text("Lengkapi semua data (termasuk tipe akun & 3 hobi) untuk mengaktifkan tombol.", color = Color.Gray, fontSize = 12.sp)
        }
        
        Text("Tips: Tekan lama tombol DAFTAR untuk mereset form.", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
    }

    // TUGAS: Alert Dialog Konfirmasi saat Submit
    if (showConfirmSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmSubmitDialog = false },
            title = { Text("Konfirmasi Pendaftaran") },
            text = { Text("Apakah data yang Anda masukkan sudah benar? Anda mendaftar sebagai $selectedUserType.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmSubmitDialog = false
                    onRegisterSuccess()
                }) { Text("YA, DAFTAR") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmSubmitDialog = false }) { Text("PERIKSA LAGI") }
            }
        )
    }

    // Reset Confirmation Dialog (Long Press)
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Form") },
            text = { Text("Apakah Anda yakin ingin menghapus semua data registrasi?") },
            confirmButton = {
                TextButton(onClick = {
                    name = ""; email = ""; password = ""; confirmPassword = ""
                    gender = ""; hobbies.clear(); selectedUserType = ""
                    showResetDialog = false
                }) { Text("YA, RESET") }
            },
            dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("BATAL") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TopUpForm(modifier: Modifier = Modifier, onLogout: () -> Unit) {
    var gameId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var securityPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    val selectedGames = remember { mutableStateListOf<String>() }
    var topUpAmount by remember { mutableStateOf("") }
    
    var showDialog by remember { mutableStateOf(false) }
    var showLongPressAction by remember { mutableStateOf(false) }

    val isGameIdValid = gameId.isNotBlank()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPinValid = securityPin.length >= 6
    val isPinMatch = securityPin == confirmPin && confirmPin.isNotEmpty()
    val isRegionValid = region.isNotEmpty()
    val isGamesValid = selectedGames.size >= 3
    val isAmountValid = topUpAmount.isNotEmpty()

    val canSubmit = isGameIdValid && isEmailValid && isPinValid && isPinMatch && 
                    isRegionValid && isGamesValid && isAmountValid

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("✧ Kamael Store TopUp ✧", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Beli Diamonds & Currency Game Favoritmu")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Detail Akun & Validasi PIN", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = gameId, onValueChange = { gameId = it }, label = { Text("Game ID") }, modifier = Modifier.fillMaxWidth(), isError = gameId.isNotEmpty() && !isGameIdValid)
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Konfirmasi") }, modifier = Modifier.fillMaxWidth(), isError = email.isNotEmpty() && !isEmailValid)
                OutlinedTextField(value = securityPin, onValueChange = { securityPin = it }, label = { Text("Pin (min 6)") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation(), isError = securityPin.isNotEmpty() && !isPinValid)
                OutlinedTextField(value = confirmPin, onValueChange = { confirmPin = it }, label = { Text("Ulangi Pin") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation(), isError = confirmPin.isNotEmpty() && !isPinMatch)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Region", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Row {
                    RadioButton(selected = region == "ID", onClick = { region = "ID" })
                    Text("Indonesia", Modifier.align(Alignment.CenterVertically))
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = region == "Global", onClick = { region = "Global" })
                    Text("Global", Modifier.align(Alignment.CenterVertically))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Game Favorit (Min 3)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                val games = listOf("MLBB", "Free Fire", "PUBG", "Genshin", "HOK", "Valorant")
                games.forEach { game ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = selectedGames.contains(game), onCheckedChange = { if(it) selectedGames.add(game) else selectedGames.remove(game) })
                        Text(game)
                    }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nominal", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                var expanded by remember { mutableStateOf(false) }
                val amounts = listOf("50", "250", "500", "1000", "5000")
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(value = topUpAmount, onValueChange = {}, readOnly = true, label = { Text("Pilih Nominal") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth() )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        amounts.forEach { amt -> DropdownMenuItem(text = { Text("$amt Diamonds") }, onClick = { topUpAmount = amt; expanded = false }) }
                    }
                }
            }
        }

        val interactionSource = remember { MutableInteractionSource() }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = canSubmit,
                    onClick = { showDialog = true },
                    onLongClick = { showLongPressAction = true }
                ),
            shape = RoundedCornerShape(12.dp),
            color = if (canSubmit) MaterialTheme.colorScheme.primary else Color.Gray,
            contentColor = Color.White
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Default.ShoppingCart, null)
                Spacer(Modifier.width(8.dp))
                Text("BAYAR SEKARANG", fontWeight = FontWeight.Bold)
            }
        }
        
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("LOGOUT") }
        Spacer(modifier = Modifier.height(40.dp))
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, title = { Text("Konfirmasi") }, text = { Text("Kirim top up senilai $topUpAmount ke ID $gameId?") }, confirmButton = { TextButton(onClick = { showDialog = false }) { Text("YA") } }, dismissButton = { TextButton(onClick = { showDialog = false }) { Text("BATAL") } })
    }

    if (showLongPressAction) {
        AlertDialog(onDismissRequest = { showLongPressAction = false }, title = { Text("Aksi Long Press") }, text = { Text("Ingin mereset form top up?") }, confirmButton = { TextButton(onClick = { gameId = ""; email = ""; securityPin = ""; confirmPin = ""; region = ""; selectedGames.clear(); topUpAmount = ""; showLongPressAction = false }) { Text("RESET") } }, dismissButton = { TextButton(onClick = { showLongPressAction = false }) { Text("BATAL") } })
    }
}
