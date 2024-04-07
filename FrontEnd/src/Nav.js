import logo from './logo.svg';
import './App.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { User } from './UserContext';
import DrawerNav from './DrawerNav';
import './Nav.css'

function Nav() {

   
const {createUser}= User()
const {States}= User()
const {authState}= User()
const {setAuthState}= User()
const {userToken}= User()
const {SERVER_URL}= User()
const {login}= User()
const {logout}= User()
const {setLoginState}= User()
const {loginRejected}= User()
const {userTransactions}= User()

  return (
    <div>

      {/* small to xl screen */}

      <AppBar position="static" sx={{display:{xs:'none',md:'flex'}}}>
      <Toolbar classes={{ root: "nav" }} >
      <a href='/' style={{color:'white',textDecoration:'None'}}><Typography variant="h5">EXCHANGE RATE</Typography> </a>
      <Box>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/transactions'>Rates</a>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/offers'  >Transactions</a>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/calculator'>Offers</a>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/offers'  >Calculator</a>

      </Box>
        <Box sx={{marginRight:'2%'}}>
        {userToken !== null ? ( <Button color="inherit" onClick={logout}>Logout</Button>) : (
        <Box>
            <Button color="inherit" onClick={() => setAuthState(States.USER_CREATION)}>Register</Button>
            <Button color="inherit" onClick={() => setAuthState(States.USER_LOG_IN)}> Login  </Button>
        </Box>)}
        </Box>
      </Toolbar>
    </AppBar>
    

    {/* xs screen */}

    <AppBar position="static" sx={{display:{xs:'flex',md:'none'}}}>
      <Toolbar classes={{ root: "nav" }} >

      <DrawerNav/>

      <a  href='/' style={{display:'flex',flexDirection:'row','&:hover': {opacity:0.6,transition: 'opacity .25s ease-in-out'},textDecoration:'white',color:'white',justifyContent:'center',width:'20%',height:'100%', mx:'auto'}}>
          E.R
      </a>

        <Box sx={{marginRight:'2%'}}>
        {userToken !== null ? ( <Button color="inherit" onClick={logout}>Logout</Button>) : (
        <Box>
            <Button color="inherit" onClick={() => setAuthState(States.USER_CREATION)}>Register</Button>
            <Button color="inherit" onClick={() => setAuthState(States.USER_LOG_IN)}> Login  </Button>
        </Box>)}
        </Box>
      </Toolbar>
    </AppBar>

    <UserCredentialsDialog open={authState===States.USER_CREATION} title={"Register"} onSubmit={createUser} onClose={()=>{setAuthState(States.PENDING)}} submitText={"Register"} />
    <UserCredentialsDialog open={authState===States.USER_LOG_IN} title={"Login"} onSubmit={login} onClose={()=>{setAuthState(States.PENDING)}} submitText={"Login"} />
    <Snackbar elevation={6}variant="filled" open={loginRejected} autoHideDuration={2000} onClose={() => setLoginState(false)}>
        <Alert severity="error">Wrong Username or Password</Alert>
    </Snackbar>
    <Snackbar elevation={6}variant="filled" open={authState === States.USER_AUTHENTICATED} autoHideDuration={2000} onClose={() => setAuthState(States.PENDING)}>
        <Alert severity="success">Success</Alert>
    </Snackbar>

    
    </div>
  );
}

export default Nav;
