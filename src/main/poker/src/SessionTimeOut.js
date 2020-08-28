import React, { Component } from 'react';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import { Container, Row, Col } from 'reactstrap';
import { Form } from 'reactstrap';
import './SessionTimeOut.css';


class SessionTimeOut extends Component {

  render() {
    return (
      <div class="body">
        <Container class="container">
          <Row>
            <Col md={{ size: 6, offset: 3 }}>
          		<h4>セッションタイムアウトもしくはアクセストークンの期限が切れました。</h4>
          	</Col>
          </Row>
          <Row>
            <Col md={{ size: 6, offset: 3 }}>
          		<h4>恐れ入りますが、もう一度ログインしなおしてください。</h4>
          	</Col>
          </Row>
          <Row>
            <Col md={{ size: 6, offset: 5 }}>
      	  	  <a href="http://localhost:8083/login">ログイン画面へ</a>
      	  	</Col>
        	</Row>
      	</Container>
	    </div>
    );
  }
}


export default withRouter(SessionTimeOut);