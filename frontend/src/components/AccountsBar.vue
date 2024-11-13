<template>
    <v-row class="pa-4 align-center">
        <v-col cols="12" md="6" class="pr-2">
            <v-text-field
                variant="outlined"
                v-model="searchQuery"
                label="Search user name"
                prepend-inner-icon="mdi-magnify"
                hide-details
                full-width
            ></v-text-field>
        </v-col>

        <v-col cols="6" md="3">
            <v-btn color="secondary" class="ml-0" block @click="showCreateAccountDialog = true">
                <v-icon class="mr-2" left>mdi-update</v-icon>
                Update
            </v-btn>
        </v-col>

        <v-col cols="6" md="3">
            <v-btn color="primary" class="ml-0" block @click="showCreateAccountDialog = true">
                <v-icon class="mr-2">mdi-account-plus</v-icon>
                Create Account
            </v-btn>
        </v-col>
    </v-row>

    <v-dialog v-model="showCreateAccountDialog" max-width="500px">
        <v-card>
            <v-card-title>
                <span class="headline">Create New Account</span>
            </v-card-title>
            <v-card-text>
                <v-form>
                    <v-text-field v-model="newUserName" label="Username" required></v-text-field>
                    <v-select
                        v-model="newUserRole"
                        :items="userRoles"
                        label="Role"
                        required
                    ></v-select>
                    <v-text-field v-model="newUserPassword" label="Password" type="password" required></v-text-field>
                </v-form>
            </v-card-text>
            <v-card-actions>
                <v-btn color="primary" @click="createUser">Create</v-btn>
                <v-spacer></v-spacer>
                <v-btn color="error" @click="showCreateAccountDialog = false">Cancel</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
export default {
    data() {
        return {
            searchQuery: '',
            showCreateAccountDialog: false,
            newUserName: '',
            newUserPassword: '',
            newUserRole: '',
            userRoles: ['ROLE_ADMIN', 'ROLE_MARKETING', 'ROLE_SALES'],
        };
    },
    methods: {

        async createUser() {
    const token = localStorage.getItem("jwt_token"); // Get JWT from local storage

    const userPayload = {
        username: this.newUserName,
        password: this.newUserPassword,
        role: this.newUserRole,  // Include role
    };

    console.log(token);

    try {
        const response = await fetch('http://localhost:8080/auth/createUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Attach the JWT token in the Authorization header
            },
            body: JSON.stringify(userPayload),
        });

        if (response.status === 201) {
            console.log('User created successfully');
            this.showCreateAccountDialog = false;
            this.newUserName = '';
            this.newUserPassword = '';
            this.newUserRole = '';
        } else if (response.status === 403) {
            console.error('Access Denied: Requires Admin Role');
        } else {
            console.error('Error creating user:', response.statusText);
        }
    } catch (error) {
        console.error('Error creating user:', error);
    }
}

    },
};
</script>
