package hellojpql;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

import static hellojpql.MemberType.ADMIN;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.setTeam(teamA);
            member1.setType(ADMIN);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.setTeam(teamA);
            member2.setType(ADMIN);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(10);
            member3.setTeam(teamB);
            member3.setType(ADMIN);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();
            //사용자 정의 함수
//            String query = "select function('group_concat', m.username) from Member m";

            //컬렉션 값 연관 관계: 묵시적 내부 조인 발생 & 탐색X (더 이상 필드를 추가하지 못함)
            //묵시적 조인
            String query = "select t From Team t join fetch t.memberList m";
            em.createQuery(query);

            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();

            System.out.println("result = " + result.size());
//            for(Team team : result) {
//                System.out.println("team = " + team.getName() +"|"+ team.getMemberList().size());
//                for (Member member : team.getMemberList()) {
//                    System.out.println(" -> member = " + member);
//                }
//            }
            //반환 타입이 명확할 때
//            TypedQuery<Member> selectMFromMemberM = em.createQuery("select m from Member m", Member.class);
//            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
//                    .setParameter("username", "kim")
//                    .getSingleResult();
//
//            System.out.println("result.getUsername() = " + result.getUsername());

            //결과가 반드시 하나 일 때에만 사용 (없거나 둘 이상일 때 Exception 발생함)
//            String singleResult = query.getSingleResult();

            //반환 타입이 명확하지 않을 때
//            Query query2 = em.createQuery("select m.username, m.age from Member m");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
