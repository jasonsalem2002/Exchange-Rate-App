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
    const [SERVER_URL, setServerUrl] = useState("https://scorpion-glowing-guppy.ngrok-free.app");
    let [buyUsdRate, setBuyUsdRate] = useState(null);
    let [sellUsdRate, setSellUsdRate] = useState(null);
    const [userName,setUserName]=useState('');


    function fetchRates() {
        fetch(`${SERVER_URL}/exchangeRate`)
        .then(response => {
        return response.json();
      })
        .then(data => {
        
          setBuyUsdRate( data['usd_to_lbp']);
          setSellUsdRate(data['lbp_to_usd']);
      
        });
       }

       useEffect(() => {
        const storedUserName = localStorage.getItem('username');
        if (storedUserName) {
           
            setUserName(storedUserName);
        }
    }, []);

    const login = useCallback((username, password) => {

        localStorage.setItem('username',username)
        return fetch(`${SERVER_URL}/authentication`, {
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
                console.log(response.json)
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

        localStorage.setItem('username',(username))
        return
    
        fetch(`${SERVER_URL}/user`, {
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
        fetch(`${SERVER_URL}/transaction`, {
            method:'GET',
            headers: {
                Authorization: `Bearer ${userToken}`,
            }
        })
        .then((response) => response.json())
        .then((transactions) => setUserTransactions(transactions));
    }, [SERVER_URL, userToken]);





    // useEffect(() => {
    //     if (userToken) {
    //         fetchUserTransactions();
    //     }
    // }, [fetchUserTransactions, userToken]);

    const logout = useCallback(() => {
        clearUserToken();
        setUserToken(null);
    }, []);

    return (
        <UserContext.Provider value={{ logout, authState, setAuthState, saveUserToken, States, login, createUser, userToken, fetchUserTransactions, SERVER_URL, loginRejected, setLoginState, setUserToken, setUserTransactions,registerRejected,setRegisterState,buyUsdRate,sellUsdRate,fetchRates,userName,setUserName }}>
            {children}
        </UserContext.Provider>
    );
}

export function User() {
    return React.useContext(UserContext);
}
