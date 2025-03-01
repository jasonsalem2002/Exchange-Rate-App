import logo from './logo.svg';
import './Nav.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography,Menu } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { User } from './UserContext';
import DrawerNav from './DrawerNav';
import './Nav.css'

import ChatDrawer from './ChatSystem/ChatDrawer';
import RatesSideBar from './Rates/RatesSideBar';

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
const {setRegisterState}= User()
const {loginRejected}= User()
const {userTransactions}= User()
const {registerRejected}= User()
const {cantReachBackend}=User();
const {setCantReachBackend}=User()
const [anchorEl, setAnchorEl] = useState(null);
const handleMenu = (event) => {
  setAnchorEl(event.currentTarget);
};

const handleClose = () => {
  setAnchorEl(null);
};

  return (
    <div>

      {/* small to xl screen */}

      <AppBar position="static" sx={{display:{xs:'none',md:'flex'}}}>
      <Toolbar classes={{ root: "nav" }} >
      <a href='/' style={{color:'white',textDecoration:'None'}}><Typography variant="h5">EXCHANGE RATE</Typography> </a>
      <Box sx={{width:'50%',display:'flex',flexDirection:'row'}}>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/transactions'  >Transactions</a>
      <a className='navlink' onClick={handleMenu} style={{ color: 'white' }}>Offers</a>
            <Menu sx={{
    "& .MuiList-root.MuiMenu-list": {
      padding: 0,
    },
  }}
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem class='menuitem' onClick={handleClose}><a class='menulink' href='availible_offers'>Availible Offers </a></MenuItem>
              <MenuItem class='menuitem' onClick={handleClose}><a class='menulink' href='my_offers'>My Offers </a></MenuItem>
            </Menu>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/graph'><p>Graph</p></a>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/predictor'><p>Predictor</p></a>
      <a class="navlink"  style={{color:'white',textDecoration:'none'}} href='/statistics'><p>Statistics</p></a>

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
          E
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
    <Snackbar elevation={6}variant="filled" open={registerRejected} autoHideDuration={2000} onClose={() => setRegisterState(false)}>
        <Alert severity="error">Password must be between 8 and 64 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.</Alert>
    </Snackbar>
    <Snackbar elevation={6}variant="filled" open={cantReachBackend} autoHideDuration={2000} onClose={() => setCantReachBackend(false)}>
        <Alert severity="error">Please Turn Wifi On</Alert>
    </Snackbar>
      <Box sx={{display:'flex',flexDirection:'column',marginTop:'4%',marginRight:'0'}}>
    <ChatDrawer/>

   
    <RatesSideBar/>
    </Box>
    
    </div>
  );
}

export default Nav;
