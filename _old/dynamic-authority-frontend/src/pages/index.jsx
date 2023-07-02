import {Button, Card, Col, Form, Input, message, Row} from "antd";
import axios from "axios";
import {useRouter} from "next/router";
import {useState} from "react";
import {useDispatch} from "react-redux";
import {updateUsername} from "@/lib/redux/slices/usernameSlice";

export default function Home() {

    const router = useRouter()
    const [messageApi, contextHolder] = message.useMessage()
    const dispatch = useDispatch()

    const displayMessage = (type, content) => {
        messageApi.open({
            type: type,
            content: content
        })
    }

    const signInHandler = (result) => {
        if (result.length !== 0) {
            const {username, hasRoot} = result
            if (hasRoot) {
                // console.log(username)
                dispatch(updateUsername(username))
                router.replace("/main")
            } else {
                displayMessage("warning", "insufficient authority")
            }
        } else {
            displayMessage("error", "wrong username or password")
        }
    }

    return (
        <>
            {contextHolder}
            <Row style={{height: "100vh"}}>
                <Col span={5}></Col>
                <Col
                    span={14}
                    style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        height: "100%"
                    }}
                >
                    <Card
                        style={{
                            width: 400
                        }}
                    >
                        <SignInForm signInHandler={signInHandler}/>
                    </Card>
                </Col>
                <Col span={5}></Col>
            </Row>
        </>
    )
}

function SignInForm({signInHandler}) {

    const [loading, setLoading] = useState(false)

    const signIn = (values) => {
        setLoading(true)
        axios.post(
            "/api/user/sign-in",
            {
                username: values.username,
                password: values.password
            },
            {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                }
            }
        ).then(resp => {
            signInHandler({username: values.username, ...resp.data.data})
            setLoading(false)
        }).catch(err => {
            console.log(err)
            signInHandler("")
            setLoading(false)
        })
    }

    return (
        <Form
            name="basic"
            labelCol={{
                span: 8,
            }}
            wrapperCol={{
                span: 16,
            }}
            onFinish={signIn}
            autoComplete="off"
        >
            <Form.Item
                label="Username"
                name="username"
                rules={[
                    {
                        required: true,
                        message: "Please input your username!",
                    },
                ]}
            >
                <Input/>
            </Form.Item>
            <Form.Item
                label="Password"
                name="password"
                rules={[
                    {
                        required: true,
                        message: "Please input your password!",
                    },
                ]}
            >
                <Input.Password/>
            </Form.Item>
            <Form.Item
                wrapperCol={{
                    offset: 8,
                    span: 16,
                }}
            >
                <Button type="primary" htmlType="submit" disabled={loading}>
                    Sign In
                </Button>
            </Form.Item>
        </Form>
    );
}

