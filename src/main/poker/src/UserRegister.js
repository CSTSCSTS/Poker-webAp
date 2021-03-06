import React, { Component } from 'react';
import './userRegister.css'
import { withRouter } from 'react-router';
import CommonHeader from './commonHeader'
import {BrowserRouter, Switch, Route, Link } from 'react-router-dom';
import { Container, Row, Col, Form, Input, ButtonGroup, Button } from 'reactstrap';
import { Grid, FormGroup } from 'react-bootstrap';
import * as PokerConstNumber from './PokerConstNumber.js';
import * as PokerConstMessage from './PokerConstMessage.js';

class UserRegister extends Component {

	constructor(props) {
    super(props);
    this.state = {
      userName: '',
      password: '',
      confirmationPassword: '',
      errorMessage: []
    };
  }

	usernameHandleChange(event) {
    this.setState({userName: event.target.value});
  }

	passwordHandleChange(event) {
    this.setState({password: event.target.value});
  }

	confirmationPasswordHandleChange(event) {
    this.setState({confirmationPassword: event.target.value});
  }

	handleSubmit(e) {

		// リクエスト前に必須チェック・文字数チェック・パスワード一致チェックを実施
		var errorList = [];
		// 必須チェック
		this.nullOrEmptyCheck(errorList, this.state.userName, PokerConstMessage.userNameNotInputMessage);
		this.nullOrEmptyCheck(errorList, this.state.password, PokerConstMessage.passwordNotInputMessage);
		this.nullOrEmptyCheck(errorList, this.state.confirmationPassword, PokerConstMessage.confirmationPasswordNotInputMessage);
		// 文字数チェック
		this.lengthOverCheck(errorList, this.state.userName, PokerConstNumber.USER_NAME_MAX_LENGTH, PokerConstMessage.userNameExceedMaxLengthMessage);
		this.lengthOverCheck(errorList, this.state.password, PokerConstNumber.PASSWORD_MAX_LENGTH, PokerConstMessage.passwordExceedMaxLengthMessage);
		// パスワード一致チェック
		this.passwordSameCheck(errorList, this.state.password, this.state.confirmationPassword);

    if(errorList.length != 0) {
      this.setState({errorMessage: errorList});
      return;
    }

		var request = require('superagent');
	    e.preventDefault();
	    const url = window.location + '';
	    request
	    .post(url)
	    .type('form')
	    .send({userName: this.state.userName, password: this.state.password})
	    .then(res => {
	    	this.props.history.push({
				  pathname: '/'
			  })
	    })
			.catch(err => {
				if(err.response.body.status === PokerConstNumber.UN_EXPECTED_ERROR_CODE) {
				  // システムエラー画面へ遷移
      		this.props.history.push({
  				  pathname: '/error'
  			  })
      	}
        errorList.push(err.response.body.message)
        this.setState({errorMessage: errorList})
			});
	  }

	// キャンセルボタンをクリック
	handleCancel() {
		this.props.history.push({
			pathname: '/'
		});
	}

	// 必須チェック
	nullOrEmptyCheck(errorList, value, errorMessage) {
		if(!(value)) {
			errorList.push(errorMessage);
		}
		return errorList;
	}

  // 文字数チェック
	lengthOverCheck(errorList, value, limit, errorMessage) {
		if(value.length > limit) {
      errorList.push(errorMessage);
		}
		return errorList;
	}

	// パスワード一致チェック
	passwordSameCheck(errorList, password, confirmationPassword) {
    if(password !== confirmationPassword){
      errorList.push(PokerConstMessage.passwordNotMatchMessage);
    }
    return errorList;
	}



	render() {
    return (
      <div>
        <CommonHeader />
      	<h2 id="title">茶 圓 ポ ー カ ー ユーザー登録</h2>
      	<Container>
      	  <Row>
      	  <Col sm="12" md={{ size: 6, offset: 3 }}>
        	    <div>
                {this.state.errorMessage.map((item) => (
                  <p class="alert alert-danger">{item}</p>
                 ))}
              </div>
              </Col>
              </Row>
    	      <form>
    	        <Row>
    	          <Col sm="12" md={{ size: 6, offset: 3 }}>
    	          	<FormGroup>
    	          	<label>ユーザー名入力</label>
    	          	<Input type="text" onChange={this.usernameHandleChange.bind(this)}></Input>
                  </FormGroup>
                </Col>
              </Row>
              <Row>
  	          <Col sm="12" md={{ size: 6, offset: 3 }}>
              <FormGroup>
                <label>パスワード入力</label>
                <Input type="password" onChange={this.passwordHandleChange.bind(this)}></Input>
              </FormGroup>
              </Col>
              </Row>
              <Row>
  	          <Col sm="12" md={{ size: 6, offset: 3 }}>
              <FormGroup>
                <label>パスワード(確認)入力</label>
                <Input type="password" onChange={this.confirmationPasswordHandleChange.bind(this)}></Input>
              </FormGroup>
              </Col>
              </Row>
              <Row>
              <Col sm="12" md={{ size: 5, offset: 7 }}>
              <ButtonGroup id="user_register_buttons">
      	    	    <Button color="primary" onClick={this.handleSubmit.bind(this)}>登録</Button>
      	    	    <Button color="primary" onClick={this.handleCancel.bind(this)}>キャンセル</Button>
      	    	</ButtonGroup>
      	    	</Col>
      	    	</Row>
    	      </form>
    	    </Container>
	  </div>
    );
  }

}

export default withRouter(UserRegister);