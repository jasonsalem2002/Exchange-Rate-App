import React, { useCallback, useEffect, useState } from 'react';
import { clearUserToken, getUserToken, saveUserToken } from "./localstorage";
import { createContext } from 'react';

const UserContext = createContext();

const States = {
    PENDING: "PENDING",
    USER_CREATION: "USER_CREATION",
    USER_LOG_IN: "USER_LOG_IN",
    USER_AUTHENTICATED: "USER_AUTHENTICATED",
};



export function UserProvider({ children }) {
    const [userToken, setUserToken] = useState(getUserToken());
    const [loginRejected, setLoginState] = useState(false);
    const [registerRejected, setRegisterState] = useState(false);
    const [userTransactions, setUserTransactions] = useState([]);
    const [authState, setAuthState] = useState(States.PENDING);
    const [SERVER_URL, setServerUrl] = useState("http://192.168.31.85:4820");

    const login = useCallback((username, password) => {
        return fetch(`${SERVER_URL}/api/authentication`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                user_name: username,
                password: password
            }),
        })
        .then((response) => {

    
            if (response.status === 403|| response.status===404 || response.status === 400) {
                setLoginState(true);  
                throw new Error("Unauthorized");
            }

            else
            {
                return response.json();
            }
    })

    .catch((error) => {
        console.error("Login failed:", error.message);
        // Optionally, handle the error here or set additional state to inform the UI about the login failure
        throw error; // re-throw the error to propagate it to the next .catch() block
    })

        .then((body) => {
            setAuthState(States.USER_AUTHENTICATED);
            setUserToken(body.token);
            saveUserToken(body.token);
        });
        
    }, [SERVER_URL]);
    
    const createUser = useCallback((username, password) => {
        return fetch(`${SERVER_URL}/api/user`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                user_name: username,
                password: password,
            }),
        }).then((response) => {
            if (response.ok) {
                return login(username, password);
            } 
            
            else if (response.status === 400)
            {
                    setRegisterState(true)
            }
            
            else {
                throw new Error("User creation failed");
            }
        });
    }, [SERVER_URL, login]);

    const fetchUserTransactions = useCallback(() => {
        fetch(`${SERVER_URL}/api/transaction`, {
            method:'GET',
            headers: {
                Authorization: `Bearer ${userToken}`,
            }
        })
        .then((response) => response.json())
        .then((transactions) => setUserTransactions(transactions));
    }, [SERVER_URL, userToken]);

    useEffect(() => {
        if (userToken) {
            fetchUserTransactions();
        }
    }, [fetchUserTransactions, userToken]);

    const logout = useCallback(() => {
        clearUserToken();
        setUserToken(null);
    }, []);

    return (
        <UserContext.Provider value={{ logout, authState, setAuthState, saveUserToken, States, login, createUser, userToken, fetchUserTransactions, SERVER_URL, loginRejected, setLoginState, setUserToken, setUserTransactions,registerRejected,setRegisterState }}>
            {children}
        </UserContext.Provider>
    );
}

export function User() {
    return React.useContext(UserContext);
}
