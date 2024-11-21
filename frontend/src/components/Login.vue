<template>
    <v-container>
        <v-card max-width="80%">
            <v-card-text class="pa-6">
                <v-form>
                    <v-text-field
                        v-model="user.username"
                        label="Username"
                        outlined
                    ></v-text-field>
                    <v-text-field
                        v-model="user.password"
                        label="Password"
                        type="password"
                        outlined
                    ></v-text-field>
                </v-form>
                <v-alert v-if="errorLogin" type="error" dismissible class="mt-4">
                    {{ errorMessage }}
                </v-alert>
                <v-btn color="primary" width="100%" size="large" variant="flat" @click="login">Login</v-btn>
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script>
export default {
    name: "Login",
    data() {
        return {
            errorLogin: false,
            errorMessage: '',
            user: {
                username: '',
                password: ''
            }
        };
    },
    beforeMount() {
        // Check if JWT token exists in localStorage and validate it (e.g., non-empty).
        const token = localStorage.getItem("jwt_token");
        if (token) {
            // Optionally, add token validation logic here if needed
            this.$router.push('/profile'); // Redirect if a token exists
        }
    },
    methods: {
        async login() {
            this.errorLogin = false;
            this.errorMessage = '';

            try {
                const response = await fetch("http://localhost:8080/users/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(this.user)
                });

                if (!response.ok) {
                    if (response.status === 403) {
                        this.errorMessage = "Invalid username or password.";
                    } else if (response.status >= 500) {
                        this.errorMessage = "Server error. Please try again later.";
                    } else {
                        this.errorMessage = "Login failed. Please check your credentials.";
                    }
                    this.errorLogin = true;
                    return;
                }

                const token = await response.text();
                localStorage.setItem("jwt_token", token);
                this.$router.push('/purchaseHistory');
            } catch (error) {
                console.error("Login error:", error);
                this.errorMessage = "An unexpected error occurred. Please try again.";
                this.errorLogin = true;
            }
        }
    }
};
</script>
