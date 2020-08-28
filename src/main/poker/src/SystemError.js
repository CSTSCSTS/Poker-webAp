import React, { Component } from 'react';
import { withRouter } from 'react-router';
import { Container, Row } from 'reactstrap';
import { Link } from 'react-router-dom';


class SystemError extends Component {

  render() {
    return (
      <div>
      	<h1 id="title">茶 圓 ポ ー カ ー システムエラー</h1>
      	<Container id="form">
      	  <Row>
	          <p>予期せぬエラーが発生しました</p>
	        </Row>
	        <Row>
            <Link to="/">スタート画面へ</Link>
          </Row>
	      </Container>
	    </div>
    );
  }
}

export default withRouter(SystemError);