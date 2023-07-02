import {useState} from "react";
import {useRouter} from "next/router";
import {Button, Card, Col, Form, Input, message, notification, Row} from "antd";
import axios from "axios";

export default function Main() {

    const router = useRouter()
    const [notificationApi, contextHolder] = notification.useNotification()
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
            if (resp.data !== null && resp.data.code === 200) {
                axios.get(
                    "/api/user/role"
                ).then(resp => {
                    if (resp.data !== null) {
                        const {code, message, data: roles} = resp.data
                        if (code === 200 && roles.filter(role => role.name === 'root').length > 0) {
                            localStorage.setItem("username", values.username)
                            router.replace("/main")
                        } else {
                            notificationApi.warning({
                                message: 'insufficient authority'
                            })
                        }
                    }
                }).catch(err => {
                    console.log(err)
                });
            } else {
                notificationApi.error({
                    message: 'wrong username or password'
                })
            }
            setLoading(false)
        }).catch(err => {
            console.log(err)
        })
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
                        <Form
                            name="signInForm"
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
                    </Card>
                </Col>
                <Col span={5}></Col>
            </Row>
        </>
    )
}