import React, { useCallback, useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import { User } from '../UserContext';
import { Box, Button, TextField, Select, MenuItem, FormControl, InputLabel, Typography,Snackbar,Alert } from '@mui/material';
import Nav from '../Nav';
import '../App.css';
import './Predictor.css';

function Predictor() {
  const [data, setData] = useState([]);
  const [xAxisData, setXAxisData] = useState([]);
  const [yAxisData, setYAxisData] = useState([]);
  const [givenDate, setGivenDate] = useState('');
  const [givenDateData,setGivenDateData]=useState({})
  const [fetchedGivenDateData,setFetchedGivenDateData]=useState(false)
  const [endDate, setEndDate] = useState('');
  const [fetchedData, setFetchedData] = useState(false);
  const [granularity, setGranularity] = useState('daily');
  const { SERVER_URL } = User();
  const { userToken } = User();
  const [requirementsRejected,setRequirementsState]=useState(false)
 const {setCantReachBackend}=User()

  const fetchNext30Days = () => {
    if (!navigator.onLine) {
        
        setCantReachBackend(true);
        return;
      }
    if (userToken)
    {
    fetch(`${SERVER_URL}/next30DaysRates`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) =>{

        if (response.ok)
        {
            return response.json();
        }
        
        else if (response.status === 404)
        {
                setRequirementsState(true)
                return;
        } 

    })
      .then((data) => {
        console.log(data)
        setFetchedData(true);
        setData(data);
      });
  }};


  const fetchATGivenDate = () => {
    if (!navigator.onLine) {
        
        setCantReachBackend(true);
        return;
      }
    if (userToken)
    {
    fetch(`${SERVER_URL}/predictRate?date=${givenDate}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) =>{

        if (response.ok)
        {
            return response.json();
        }
        
        else if (response.status === 404)
        {
                setRequirementsState(true)
                return;
        } 

    })
      .then((data) => {
        console.log(data)
        setFetchedGivenDateData(true)
        setGivenDateData(data);
      });
  }};





  useEffect(() => {
    if (fetchedData) {


        const xAxis = data.map(entry => entry.date);
        const yAxis = data.map(entry => entry.average_exchange_rate);
        console.log(yAxis)
        setXAxisData(xAxis);
        setYAxisData(yAxis);
    }
  }, [data, fetchedData]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setGranularity('daily')
    fetchATGivenDate()
    
  };



  const handleDateChange = (e) => {
    const selectedDate = e.target.value;
    if (selectedDate > '2030-12-31') {
      setGivenDate('2029-12-31');
    } else {
      setGivenDate(selectedDate);
    }
  };


  useEffect(fetchNext30Days,[])

  
  return (
    <div>
      <Nav />
      <Box id='containerBox' >
        <Box sx={{display:'flex',flexDirection:'column',alignItems:'center',height:'40%',maxWidth:'100%'}}>
        {granularity === 'daily' && <Typography className='graphTitle'>Exchange Rate over Next 30 Day(s)</Typography>}
        
        <Box sx={{ width: '70%', maxWidth: '100%',maxHeight:'100%' ,minWidth: '300px', display: { xs: 'flex' } }}>
        <LineChart
            width={800}
            height={450}
            data={xAxisData.map((date, index) => ({ x: date, 'average_exchange_rate': yAxisData[index] }))}
            margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="x" tick={{ angle: -15, textAnchor: 'end' }} />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="average_exchange_rate" stroke="#8884d8" activeDot={{ r: 8 }} />
            
          </LineChart>
        </Box>
        </Box>
        <Box id='details' className='formBox'>
          <Box className='header' >
            <Typography  className='headerText'> Predict Rate Up Until 2030</Typography>
          </Box>
          <form style={{ paddingLeft: '4%', backgroundColor: 'white', display: 'flex', flexDirection: 'column', height: '60%', justifyContent: 'space-around' }} onSubmit={handleSubmit}>
            
            <TextField
              label="Date"
              type="date"
              value={givenDate}
              onChange={(e)=>{handleDateChange(e)}}
              className='formField'
              InputLabelProps={{ shrink: true }}
              max={"2030-12-31"}
              required
            />
                {fetchedGivenDateData && <Typography>Exchange Rate: {givenDateData["average_exchange_rate"]}</Typography>}
            <Button type="submit" class='formButton' variant="contained" color="primary">
              Submit
            </Button>
          </form>
           <Snackbar elevation={6}variant="filled" open={requirementsRejected} autoHideDuration={2000} onClose={() => setRequirementsState(false)}>
        <Alert severity="error">No transaction found at the given date</Alert>
    </Snackbar>
        </Box>
       
      </Box>
    </div>
  );
}

export default Predictor;
