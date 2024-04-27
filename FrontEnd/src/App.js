import './App.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { DataGrid } from '@mui/x-data-grid';


import { BrowserRouter as Router,Routes, Route, Switch,  NavLink, Redirect } from 'react-router-dom';
import { UserProvider } from './UserContext';
import Graph from './Graph/Graph';



import Transactions from './Transactions/Transactions';
import Statistics from './Statistics/Statistics';
import MyOffers from './Offers/MyOffers';
import AvailableOffers from './Offers/AvailibleOffers';
import Predictor from './Predictor/Predictor';


function App() {

  return (
    <div style={{}}>
    <UserProvider>
    <Router>
      <Routes>
        <Route path='/' element={<Transactions/>}></Route>
        <Route path='/transactions' element={<Transactions/>}></Route>
        <Route path='/availible_offers' element={<AvailableOffers/>}></Route>
        <Route path='/my_offers' element={<MyOffers/>}></Route>
        <Route path='/graph' element={<Graph/>}></Route>
        <Route path='/predictor' element={<Predictor/>}></Route>
        <Route path='/statistics' element={<Statistics/>}></Route>
      </Routes>
    </Router>
    </UserProvider>
    </div>
  );
}

export default App;
