package bot.iservice;

import bot.iservice.commands.comandosBoti;
import com.sun.tools.jconsole.JConsoleContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class botDiscord extends ListenerAdapter { // Essa é a classe principal. Você precisa dela para ativar o bot e fazer qualquer coisa nela

    public static void main(String[] args) {

        JDA boti = JDABuilder.createDefault("MTA0MjYwMDc2Mjg4NDI0MzU1Nw.GAq7p2.5gdc4gJI3TiCi-xLoO4YiF6y74wn6Ecmf8Q9vA") // Isso é a chavez para o bot entrar no perfil dele de discord. NÃO MUDA.
                .setActivity(Activity.listening("Rádio UNIFIL!")) // Esse é a atividade que ele está fazendo no perfil dele. Você pode mudar para jogae/ouvir/competindo e etc.
                .addEventListeners(new comandosBoti()) // Isso chama a classe de comandos que o bot usa para fazer tudo dele
                .enableIntents(GatewayIntent.MESSAGE_CONTENT) // Isso é importante. Por causa das politicas do discord, bots agora precisam ter um check para mandar algumas mensagens. Se o bot estiver em 100 servidores ou mais, a equipe do discord ira dar review nele e não poder ser usado por um tempo. Então só não coloca o bot em servers disnecessarios.
                .build(); // Aqui ele finalmente começa o bot.
    }
}
