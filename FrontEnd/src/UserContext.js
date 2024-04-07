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
    const [userTransactions, setUserTransactions] = useState([]);
    const [authState, setAuthState] = useState(States.PENDING);
    const [SERVER_URL, setServerUrl] = useState("http://192.168.31.85:4820");

    const login = useCallback((username, password) => {
        return fetch(`${SERVER_URL}/authentication`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                user_name: username,
                password: password,
            }),
        })
        .then((response) => {
            if (response.status === 403) {
                setLoginState(true);
            }
            return response.json();
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
            } else {
                throw new Error("User creation failed");
            }
        });
    }, [SERVER_URL, login]);

    const fetchUserTransactions = useCallback(() => {
        fetch(`${SERVER_URL}/api/transaction`, {
            headers: {
                Authorization: `Bearer ${userToken}`,
            },
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
        <UserContext.Provider value={{ logout, authState, setAuthState, saveUserToken, States, login, createUser, userToken, fetchUserTransactions, SERVER_URL, loginRejected, setLoginState, setUserToken, setUserTransactions }}>
            {children}
        </UserContext.Provider>
    );
}

export function User() {
    return React.useContext(UserContext);
}
