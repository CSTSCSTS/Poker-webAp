import React, { Component } from 'react';
import './pokerStart.css';
import logo from './logo.svg';
import './App.css';
import Potal from './Potal';
import PokerField from './PokerField';
import UserRegister from './UserRegister'
import SystemError from './SystemError'
import SessionTimeOut from './SessionTimeOut'
import Forbidden from './forbidden'
import CommonHeader from './commonHeader'
import Bet from './bet';
import { withRouter } from 'react-router';
import {BrowserRouter, Switch, Route, Link } from 'react-router-dom';
import { Button, Container, Row, Col, Form, FormGroup, Input } from 'reactstrap';
import * as PokerConstNumber from './PokerConstNumber.js';
import * as PokerConstMessage from './PokerConstMessage.js';
import LoginServicePopUp from './LoginServicePopUp';
import Modal from "react-modal";

class App extends Component {
  render() {
    return (
      <BrowserRouter>
          <div>
            <Switch>
              <Route exact path={'/'} component={PokerStart}/>
              <Route exact path={'/potal'} component={Potal}/>
              <Route exact path={'/user'} component={UserRegister}/>
              <Route exact path={'/play'} component={PokerField}/>
              <Route exact path={'/error'} component={SystemError}/>
              <Route exact path={'/forbidden'} component={Forbidden}/>
              <Route exact path={'/session-timeout'} component={SessionTimeOut}/>
            </Switch>
          </div>
        </BrowserRouter>
    );
  }
}

class PokerStart extends Component {

  constructor(props) {
    super(props);
    this.state = {
      jokerIncluded: true
    };
  }

  handleSubmit(e) {
	var request = require('superagent');
    e.preventDefault();
    const url = '/bet'
    request
    .get(url)
    .responseType('text')
    .then(res => {
      this.handleToBet(res.body, this.state.jokerIncluded);
    })
    // システムエラー画面へ遷移
    .catch(err => {
    	if(err.response.body.status === PokerConstNumber.UN_AUTHORIZE_ERROR_CODE) {
    		this.props.history.push({
      		pathname: '/session-timeout'
      	})
      	return;
    	}
    	if(err.response.body.status === PokerConstNumber.UN_EXPECTED_ERROR_CODE) {
    	  this.props.history.push({
    		  pathname: '/error'
    	  })
    	  return;
    	}
    });


  }
    handleToBet = (body, jokerIncluded) => {
    	this.props.history.push({
    		pathname: '/play',
    		state: {betMoney: body, jokerIncluded: jokerIncluded}
    	})
    }

  handleChange(event) {
	const jokerIncluded = event.target.value == 'included' ? true : false;
    this.setState({jokerIncluded: jokerIncluded});
  }

  render() {
    return (
      <div>
        <CommonHeader />
        <LoginServicePopUp
          isOpen={this.props.location.state === undefined ? false : this.props.location.state.isOpen}
          username={this.props.location.state === undefined ? '' : this.props.location.state.userName}
        />
      	<h1 id="title">茶 圓 ポ ー カ ー</h1>
	    <Container id="form">
	      <Form>
	        <FormGroup>
	          <Input type="select" onChange={this.handleChange.bind(this)} name="select" id="exampleSelect">
	            <option value="included">ジョーカーを含む</option>
	            <option value="non-included">ジョーカーを含まない</option>
	          </Input>
	        </FormGroup>
	    	<Button color="primary" size="lg" block onClick={this.handleSubmit.bind(this)}>START</Button>
	      </Form>
	    </Container>
	  </div>
    );
  }
}

export default App;
