<template>
    <v-container>
        <v-card max-width="80%">
            <v-card-text>
                <v-form>
                    <v-text-field
                        label="Username"
                        outlined
                    ></v-text-field>
                    <v-text-field
                        label="Password"
                        outlined
                    ></v-text-field>
                </v-form>
            </v-card-text>
            <v-card-actions>
                <v-btn color="primary" @click="login">Login</v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                errorLogin: false,
                user: {}
            };
        },
        methods: {
            login() {
                this.errorLogin = false
                this.$store.dispatch(`auth/${AUTH_REQUEST}`, this.user)
                .then(() => {
                this.$router.push('/');
                }).catch(() => {
                this.errorLogin = true
                });
            // this.loading = false
            }
        }
    };
</script>