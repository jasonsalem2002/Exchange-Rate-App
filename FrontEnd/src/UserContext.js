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
    const [SERVER_URL, setServerUrl] = useState("https://jason.hydra-polaris.ts.net");
    let [buyUsdRate, setBuyUsdRate] = useState(null);
    let [sellUsdRate, setSellUsdRate] = useState(null);
    const [userName,setUserName]=useState('');
    const [messages, setMessages] = useState([]);
    const[usernames,setUserNames]=useState([])
    const [groups,setGroups]=useState([])
    const [joinedGroups,setJoinedGroups]=useState([])
    const [groupMessages,setGroupMessages]=useState([])
    const [groupsMessages, setGroupsMessages] = useState({});

   
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

    //    useEffect(() => {
    //     const storedUserName = localStorage.getItem('username');
    //     if (storedUserName) {
           
    //         setUserName(storedUserName);
    //     }
    // }, []);


    const login = useCallback((username, password) => {
        localStorage.setItem('username', username);
    
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
            if (response.status === 403 || response.status === 404 || response.status === 400) {
                setLoginState(true);
                throw new Error("Unauthorized");
            } else {
                return response.json();
            }
        })
        .then((body) => {
            if (body && body.token) {
                setAuthState(States.USER_AUTHENTICATED);
                setUserToken(body.token);
                saveUserToken(body.token);
            } else {
                // Handle case where token is undefined
                console.error("Token is undefined");
                setLoginState(true);
                throw new Error("Token is undefined");
            }
        })
        .catch((error) => {
            console.error("Login failed:", error.message);
            throw error;
        });
    }, [SERVER_URL]);
    

    const createUser = useCallback((username, password) => {

        localStorage.setItem('username',(username))
        return fetch(`${SERVER_URL}/user`, {
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


    const fetchUserTransactions =() => {
        if (userToken)
        {
        fetch(`${SERVER_URL}/transaction`, {
            method:'GET',
            headers: {
                Authorization: `Bearer ${userToken}`,
            }
        })
        .then((response) => response.json())
        .then((transactions) => setUserTransactions(transactions));
    }};





    // useEffect(() => {
    //     if (userToken) {
    //         fetchUserTransactions();
    //     }
    // }, [fetchUserTransactions, userToken]);


    const logout = useCallback(() => {
        localStorage.setItem('username','')
        clearUserToken();
        setUserToken(null);
    }, []);


   

    const fetchUsernames = () => {
        
        if (userToken)
        {
        const username =localStorage.getItem('username')
        console.log(username)
        fetch(`${SERVER_URL}/usernames`, {
            method:'GET',
            headers: {
                Authorization: `Bearer ${userToken}`,
            },
        })
        .then((response) => response.json())
        .then((usernames) => {setUserNames(usernames);
        
      });
    }
    }
    
      const fetchMessages = () => {

        if (userToken)
        {
        const username = localStorage.getItem('username')
        fetch(`${SERVER_URL}/chat/${username}`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${userToken}`,
          },
        })
          .then((response) =>  response.json())
          .then((messages) => setMessages((messages)));
        }}

      const fetchGroups = () => {

        if (userToken)
        {
       
        fetch(`${SERVER_URL}/groups`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${userToken}`,
          },
        })
          .then((response) =>  response.json())
          .then((groups) => setGroups((groups)));
        }}

      const fetchJoinedGroups = () => {

        if (userToken)
        {
    
        fetch(`${SERVER_URL}/my-groups`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${userToken}`,
          },
        }).then((response) =>  response.json())
            .then((joinedGroups) => setJoinedGroups((joinedGroups)));
        }}


        const joinGroup= useCallback((group)=>{ 
   
            fetch(`${SERVER_URL}/group/${group}/join`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${userToken}`,
              },
              
            })
            .then(response => {
              return response.json();
            })
            
            .then((body) => {
              fetchJoinedGroups()
          });
          })

          const createGroup= useCallback((name)=>{ 
   
             
            fetch(`${SERVER_URL}/group`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${userToken}`,
              },
              body: JSON.stringify({
                "name":name,
              })
            })
            .then(response => {
              return response.json();
            })
            
            .then((body) => {
              joinGroup(name)
              fetchJoinedGroups();
          });
          })


          const fetchGroupMessages = (group) => {
            if (userToken) {
                fetch(`${SERVER_URL}/group/${group}/messages`, {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${userToken}`,
                    },
                })
                .then((response) => response.json())
                .then((groupMessagesData) => {
                  
                    setGroupsMessages((prevGroupsMessages) => ({
                        ...prevGroupsMessages,
                        [group]: groupMessagesData,
                    }));
                })
                .catch((error) => {
                    console.error("Failed to fetch group messages:", error);
                });
            }
        };
        
        

          const sendGroupMessage= useCallback((group,message)=>{ 
            console.log(message)
            if (message.length<1)
            {
              return
            }
          
            fetch(`${SERVER_URL}/group/${group}/message`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${userToken}`,
              },
              body: JSON.stringify({
                "name":group,
                "content":message
        
              })
            })
            .then(response => {
              return response.json();
            })
            
            .then((body) => {
              fetchGroupMessages(group)
        
          });
          })

    return (
        <UserContext.Provider value={{ setJoinedGroups,sendGroupMessage,groupsMessages,fetchGroupMessages,createGroup,joinGroup,fetchMessages,fetchGroups,fetchJoinedGroups,joinedGroups,groups,fetchUsernames,usernames,messages,logout, authState, setAuthState, saveUserToken, States, login, createUser, userToken, fetchUserTransactions, userTransactions,SERVER_URL, loginRejected, setLoginState, setUserToken, setUserTransactions,registerRejected,setRegisterState,buyUsdRate,sellUsdRate,fetchRates,userName,setUserName }}>
            {children}
        </UserContext.Provider>
    );
}

export function User() {
    return React.useContext(UserContext);
}
