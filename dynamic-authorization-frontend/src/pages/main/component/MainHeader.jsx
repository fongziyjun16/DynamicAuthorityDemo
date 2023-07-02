import {Button, Col, Row} from "antd";
import Link from "next/link";
import {useRouter} from "next/router";

export default function MainHeader() {
    const router = useRouter()

    return (
        <>
            <Row>
                <Col span={12}>
                    <Link href={"/main"}>
                                <span style={{color: "white"}}>
                                    Dynamic Authorization
                                </span>
                    </Link>
                </Col>
                <Col span={12} style={{textAlign: "right"}}>
                    <Button onClick={() => router.replace("/")}>
                        Sign Out
                    </Button>
                </Col>
            </Row>
        </>
    )
}